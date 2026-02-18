package com.solvd.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.util.Optional;

public class MitmProxyClient {

    private final String baseUrl; // already includes /session/{id}/proxy/{id}
    private final Optional<String> authHeaderValue;

    public MitmProxyClient(String baseUrl) {
        this(baseUrl, Optional.empty());
    }

    public MitmProxyClient(String baseUrl, Optional<String> authHeaderValue) {
        this.baseUrl = stripTrailingSlash(baseUrl);
        this.authHeaderValue = authHeaderValue;
    }

    public static MitmProxyClient fromSeleniumHubAndSession(URL seleniumHubUrl, String sessionId) {
        String hubBase = normalizeHubBase(seleniumHubUrl.toString());
        String mitmBase = hubBase + "/proxy/" + urlEncodePath(sessionId);
        return new MitmProxyClient(mitmBase);
    }

    public Path downloadHar(String flowSelector, Path targetDir) throws IOException {
        return downloadToFile("GET", "/download/har/" + urlEncodePath(flowSelector), null, targetDir,
                "mitm-" + flowSelector + ".har");
    }

    public Path downloadDump(String flowSelector, Path targetDir) throws IOException {
        return downloadToFile("GET", "/download/dump/" + urlEncodePath(flowSelector), null, targetDir,
                "mitm-" + flowSelector + ".dump");
    }

    public String restartMitm() throws IOException {
        return requestText("POST", "/mitm-restart", null, "application/json");
    }

    public String clearFlows() throws IOException {
        return requestText("DELETE", "/clear-flows", null, "application/json");
    }

    // ---------------- internals ----------------

    private static String normalizeHubBase(String seleniumUrl) {
        String s = seleniumUrl.trim();
        while (s.endsWith("/")) s = s.substring(0, s.length() - 1);
        if (s.endsWith("/wd/hub")) {
            s = s.substring(0, s.length() - "/wd/hub".length());
        }
        return s;
    }

    private Path downloadToFile(String method,
                                String path,
                                String bodyOrNull,
                                Path targetDir,
                                String fallbackFileName) throws IOException {
        Files.createDirectories(targetDir);

        HttpURLConnection conn = open(method, path);
        conn.setRequestProperty("Accept", "*/*");

        if (bodyOrNull != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            try (OutputStream os = conn.getOutputStream()) {
                os.write(bodyOrNull.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            }
        }

        int code = conn.getResponseCode();
        InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();

        if (code < 200 || code >= 300) {
            String err = readAll(is);
            conn.disconnect();
            throw new IOException("MITM download failed: " + method + " " + (baseUrl + path) + " HTTP " + code + " body=" + err);
        }

        String fileName = filenameFromContentDisposition(conn.getHeaderField("Content-Disposition"))
                .orElse(fallbackFileName);

        Path out = targetDir.resolve(fileName);

        try (InputStream in = is;
             OutputStream os = Files.newOutputStream(out, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            byte[] buf = new byte[8192];
            int r;
            while ((r = in.read(buf)) != -1) {
                os.write(buf, 0, r);
            }
        } finally {
            conn.disconnect();
        }

        return out;
    }

    private String requestText(String method, String path, String body, String contentType) throws IOException {
        HttpURLConnection conn = open(method, path);
        conn.setRequestProperty("Accept", contentType);

        if (body != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", contentType + "; charset=utf-8");
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            }
        }

        int code = conn.getResponseCode();
        InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
        String resp = readAll(is);

        conn.disconnect();

        if (code < 200 || code >= 300) {
            throw new IOException("MITM request failed: " + method + " " + (baseUrl + path) + " HTTP " + code + " body=" + resp);
        }
        return resp;
    }

    private HttpURLConnection open(String method, String path) throws IOException {
        URL url = new URL(baseUrl + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(15_000);
        conn.setReadTimeout(60_000);
        authHeaderValue.ifPresent(v -> conn.setRequestProperty("Authorization", v));
        return conn;
    }

    private static String readAll(InputStream is) throws IOException {
        if (is == null) return "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append('\n');
            return sb.toString();
        }
    }

    private static Optional<String> filenameFromContentDisposition(String cd) {
        if (cd == null) return Optional.empty();
        int idx = cd.toLowerCase().indexOf("filename=");
        if (idx == -1) return Optional.empty();

        String v = cd.substring(idx + "filename=".length()).trim();
        if (v.startsWith("\"") && v.endsWith("\"") && v.length() >= 2) {
            v = v.substring(1, v.length() - 1);
        }
        if (v.isBlank()) return Optional.empty();
        return Optional.of(v);
    }

    private static String stripTrailingSlash(String s) {
        if (s == null) return null;
        while (s.endsWith("/")) s = s.substring(0, s.length() - 1);
        return s;
    }

    private static String urlEncodePath(String segment) {
        try {
            return java.net.URLEncoder.encode(segment, java.nio.charset.StandardCharsets.UTF_8)
                    .replace("+", "%20");
        } catch (Exception e) {
            return segment;
        }
    }

    public Path downloadHarOrDumpBestEffort(Path targetDir) throws IOException {
        String[] selectors = new String[] { "@all", "all", "*" };

        IOException last = null;
        for (String s : selectors) {
            try { return downloadHar(s, targetDir); }
            catch (IOException e) { last = e; }

            try { return downloadDump(s, targetDir); }
            catch (IOException e) { last = e; }
        }
        throw last != null ? last : new IOException("Unable to export HAR/DUMP using known selectors");
    }
}

package utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import static utils.Constants.*;


public class WebPage {

    private String content;
    private String url;

    private WebPage(String url, String content) {
        this.url = url;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public static WebPage loadWebPage(String address, String encoding) throws IOException {
        return loadWebPage(address, encoding, null);
    }

    public static WebPage loadWebPage(String address, String encoding, String postData) throws IOException {
        URL url = new URL(address);
        URLConnection conn = url.openConnection();
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.75 Safari/537.1");

        if (postData != null)
            writePostData(postData, conn);

        encoding = findRealEncoding(encoding, conn);
        String response = readResponse(conn, encoding);
        return new WebPage(address, response);
    }

    private static String findRealEncoding(String presetEncoding, URLConnection connection) {
        String realEncoding = connection.getContentEncoding();
        if (realEncoding != null)
            return realEncoding;

        String type = connection.getContentType();
        if (type != null) {
            final String CHARSET_ATTRIBUTE = "charset=";
            int pos = type.indexOf(CHARSET_ATTRIBUTE);
            if (pos != NOT_FOUND)
                return type.substring(pos + CHARSET_ATTRIBUTE.length() );
        }

        return presetEncoding;
    }

    private static void writePostData(String postData, URLConnection connection) throws IOException {
        connection.setDoOutput(true);
        OutputStreamWriter writter = new OutputStreamWriter( connection.getOutputStream() );
        writter.write(postData);
        writter.flush();
        writter.close();
    }

    private static String readResponse(URLConnection connection, String encoding) throws IOException {
        if ( !isValidContentType(connection) )
            throw new IOException("Invalid content type for " + connection.getURL());

        BufferedReader reader = new BufferedReader( new InputStreamReader(connection.getInputStream(), encoding) );

        String line, lines = "";
        while (( line = reader.readLine() ) != null)
            lines += line + "\n";

        reader.close();
        return lines;
    }

    private static boolean isValidContentType(URLConnection connection) {
        String type = connection.getContentType();
        boolean isValid = true;
        if (type != null && type.length() >= 5) {
            String category = type.substring(0, 5);
            isValid = !category.equals("image") && !category.equals("audio") && !category.equals("video");
        }
        return isValid;
    }

}

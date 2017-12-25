package com.example.icecreamcake.praktikumxml;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParserException;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "https://praktikum-xml.netlify.com/praktikum.xml";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.textView);
        Button btn = (Button) findViewById(R.id.webParse);
        //start parsing XML
        tv.setText("Daftar Buku");
        try {
            InputStream is = getAssets().open("praktikum.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            Document doc = dbBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();
            NodeList nodeList = doc.getElementsByTagName("buku");
            for(int i=0;i<nodeList.getLength();i++) {
                Node buku = nodeList.item(i);
                String ISBN = buku.getChildNodes().item(1).getTextContent();
                String judul = buku.getChildNodes().item(3).getTextContent();
                String pengarang = buku.getChildNodes().item(5).getTextContent();
                tv.setText(tv.getText()+"\nISBN : "+ISBN);
                tv.setText(tv.getText()+"\nJudul Buku : "+judul);
                tv.setText(tv.getText()+"\nPengarang : "+pengarang);
            }
        }catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);
            toast.show();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadXMLTask().execute(URL);
            }
        });
    }

    private class DownloadXMLTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadXMLFromNetwork(urls[0]);
            } catch (XmlPullParserException e) {
                return getResources().getString(R.string.connection_error);
            } catch (IOException e) {
                return getResources().getString(R.string.xml_error);
            }
        }

        @Override
        protected void onPostExecute(String result){
            WebView wb = (WebView) findViewById(R.id.webParse_result);
            wb.loadData(result, "text/html", null);
        }
    }

    private String loadXMLFromNetwork(String urlString) throws IOException, XmlPullParserException {
        InputStream stream = null;
        XMLParser feedXMLParser = new XMLParser();
        List<XMLParser.Buku> bukus = null;
        String url = null;
        StringBuilder htmlString = new StringBuilder();
        try {
            stream = downloadURL(urlString);
            bukus = feedXMLParser.parse(stream);
        } finally {
            if (stream != null){
                stream.close();
            }
        }
        for (XMLParser.Buku buku : bukus){
            htmlString.append("<p>Judul Buku: "+buku.judul+"<p>");
            htmlString.append("<p>Nama Pengarang: "+buku.pengarang+"<p>");
            htmlString.append("<p>Kode Buku: "+buku.kode+"<p>");
            htmlString.append("<br/>");
        }
        return htmlString.toString();
    }

    private InputStream downloadURL(String urlString) throws IOException{
        URL url = new URL(urlString);
        HttpURLConnection cnn = (HttpURLConnection) url.openConnection();
        cnn.setReadTimeout(10000);
        cnn.setConnectTimeout(15000);
        cnn.setRequestMethod("GET");
        cnn.setDoInput(true);
        cnn.connect();
        InputStream stream = cnn.getInputStream();
        return stream;
    }
}

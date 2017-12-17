package com.example.icecreamcake.praktikumxml;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.textView);
        Button btn = (Button) findViewById(R.id.webParse);
        //start parsing XML
        //https://praktikum-xml.netlify.com/praktikum.xml
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

            }
        });
    }
}

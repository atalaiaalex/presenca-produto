package br.com.supermercadoatalaia.presencaproduto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    private final int PERMISSAO_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermissoes();

        String host = "ERRO!!!";

        {
            try {
                salvarTxt();
                Toast.makeText(this, "Tentando ler", Toast.LENGTH_LONG).show();
                host = lerTxt();
                //Toast.makeText(this, host, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
               /* try {
                    //Toast.makeText(this, "Tentando salvar", Toast.LENGTH_LONG).show();
                    salvarTxt();
                    Toast.makeText(this, host, Toast.LENGTH_LONG).show();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }*/e.printStackTrace();
            }
        }while(host.equals("ERRO!!!"));

        Toast.makeText(this, host, Toast.LENGTH_LONG).show();

        /*
        Passar a string host para cada camada, indo do controller até o repository
        Mas tentar no repository ler o txt e mandar uma exception, indo até a view que recebendo
         vai iniciar o processo de salvar um txt pedido a configuração do host
         */
    }

    private void initPermissoes () {
        //USUARIO DAR A PERMISSAO PARA LER
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_REQUEST);
            }
        }

        //USUARIO DAR A PERMISSAO PARA ESCREVER
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSAO_REQUEST);
            }
        }
    }

    public String lerTxt() throws Exception {
        //String pasta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        String pasta = getExternalFilesDir("config").getAbsolutePath(); //Só não testei

        String nomeTxt ="app.conf";
        //Toast.makeText(this, pasta, Toast.LENGTH_LONG).show();
        BufferedReader leitor =
                new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(pasta+"/"+nomeTxt), Charset.defaultCharset()
                        )
                );

        if(leitor.ready()){
            return leitor.readLine();
        }

        return "";
    }

    public void salvarTxt () throws Exception {
        //String pasta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        String pasta = getExternalFilesDir("config").getAbsolutePath(); //Só não testei

        String nomeTxt ="app.conf";
        File arquivo1 = new File(pasta + "/" + nomeTxt);
        arquivo1.delete(); //Talver colocar um try
        FileWriter arquivo = new FileWriter(pasta + "/" + nomeTxt);
        PrintWriter outputStream;

        try {
            outputStream = new PrintWriter(arquivo);
            outputStream.println("http://192.168.2.181:8080");
            arquivo.flush();
            arquivo.close();

        } catch (Exception e) {
            Toast.makeText(
                    null,
                    "Erro criando arquivo: "+e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
            e.printStackTrace();
        }
    }
}
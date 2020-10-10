package br.com.supermercadoatalaia.presencaproduto.core;

import android.os.StrictMode;
import android.util.JsonReader;
import android.util.JsonWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiConsumer {
    private static String SERVER;
    public static final String REST_COLETAS = SERVER + "/coletas";
    public static final String FORNECEDOR = "/fornecedor/";
    public static final String NUMERO_NOTA_FISCAL = "/numero_nota_fiscal/";
    public static final String LANCAR = "/lancar/";
    public static final String REST_FORNECEDORES = SERVER + "/fornecedores";
    public static final String CNPJ = "/cnpj/";
    public static final String VINCULO = "/vinculo/";
    public static final String REST_PEDIDOS = SERVER + "/pedidos";
    public static final String BAIXADOS_FORNECEDOR = "/baixados_fornecedor/";
    public static final String NOTA_FISCAL = "/nota_fiscal/";
    public static final String REST_PRODUTOS = SERVER + "/produtos";
    public static final String LOJA = "/loja/";
    public static final String EAN = "/ean/";

    private HttpURLConnection httpURLConnection;
    private int HttpCodeResposta;

    public ApiConsumer(String sever) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SERVER = sever;
    }

    public void iniciarConexao(String method, URL url) throws IOException {
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(method);
    }

    public void addCabecalho(String chave, String valor) {
        httpURLConnection.setRequestProperty(chave, valor);
    }

    public OutputStream getOutputStream() throws IOException {
        return httpURLConnection.getOutputStream();
    }

    public InputStream processarComResposta() throws IOException {
        HttpCodeResposta = httpURLConnection.getResponseCode();

        if(HttpCodeResposta >= 200 && HttpCodeResposta < 300) {
            return httpURLConnection.getInputStream();
        } else if(getHttpResposta().equals(HttpResposta.NAO_ENCONTRADO)){
            throw new IOException(HttpResposta.NAO_ENCONTRADO.getMensagem());
        } else if(getHttpResposta().equals(HttpResposta.ERRO)){
            throw new IOException(HttpResposta.ERRO.getMensagem());
        } else if(getHttpResposta().equals(HttpResposta.ERRO_SERVIDOR)) {
            throw new IOException(HttpResposta.ERRO_SERVIDOR.getMensagem());
        }

        throw new IOException();
    }

    public HttpResposta getHttpResposta() {
        return HttpResposta.getHttpMensagem(HttpCodeResposta);
    }

    public JsonReader getJsonReader() throws IOException {
        return new JsonReader(
                new InputStreamReader(
                        this.processarComResposta(), "UTF-8"
                )
        );
    }

    public JsonWriter getJsonWriter() throws IOException {
        return new JsonWriter(
                new OutputStreamWriter(
                        this.getOutputStream(), "UTF-8"
                )
        );
    }

    public void fecharConexao() {
        httpURLConnection.disconnect();
    }
}

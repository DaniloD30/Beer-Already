package dao;

import java.util.List;

import model.Bebida;
import model.Cesta;
import model.Estabelecimento;
import model.Item;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitService {

    @POST("estabelecimento/")
    Call<Void> add(@Body Estabelecimento estabelecimento);

    @POST("item/")
    Call<Void> addItem(@Body Item item);

    @POST("bebidas/")
    Call<Void> addBebida(@Body Bebida bebida);

    @POST("cestas/")
    Call<Void> addCestas(@Body Cesta cesta);

    @GET("estabelecimento/")
    Call<List<Estabelecimento>> getAll();

    @GET("bebidas/")
    Call<List<Bebida>> getAllBebidas();

    @GET("item/")
    Call<List<Item>> getAllItens();

    @GET("cestas/")
    Call<List<Cesta>> getAllCestas();

    @GET("contato/{id}")
    Call<Estabelecimento> carregarContato(@Path("id") int contatoID);

    @POST("contato")
    Call<Estabelecimento> salvarContato(@Body Estabelecimento contato);

    @PUT("estabelecimento/{id}/")
    Call<Void> alterarEstabelecimento(@Path("id") int id, @Body Estabelecimento contato);

    @PUT("bebidas/{id}/")
    Call<Void> alterarBebida(@Path("id") int id, @Body Bebida contato);

    @DELETE("estabelecimento/{id}/")
    Call<Void> excluirEstabelecimento(@Path("id") int id);

    @DELETE("bebidas/{id}/")
    Call<Void> excluirBebida(@Path("id") int id);

    @DELETE("item/{id}/")
    Call<Void> excluirItem(@Path("id") int id);
}

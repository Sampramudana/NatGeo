package com.example.sampramudana.natgeo.Network;

import com.example.sampramudana.natgeo.Model.ResponseNatgeo;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("top-headlines?sources=national-geographic&apiKey=0a3624cc60104378b8ac6bb15d4dcd78")
    Call<ResponseNatgeo> readNewsApi();

}

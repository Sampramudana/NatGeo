package com.example.sampramudana.natgeo.Activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sampramudana.natgeo.Model.ArticlesItem;
import com.example.sampramudana.natgeo.Model.ResponseNatgeo;
import com.example.sampramudana.natgeo.Model.Source;
import com.example.sampramudana.natgeo.Network.ApiService;
import com.example.sampramudana.natgeo.Network.InstanceRetrofit;
import com.example.sampramudana.natgeo.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView rcNatgeo;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO Initialize widget to variable
        rcNatgeo = findViewById(R.id.rcNatgeo);

        getData();
    }

    private void getData() {
        final ApiService apiService = InstanceRetrofit.getInstance();
        Call<ResponseNatgeo> call = apiService.readNewsApi();
        call.enqueue(new Callback<ResponseNatgeo>() {
            @Override
            public void onResponse(Call<ResponseNatgeo> call, Response<ResponseNatgeo> response) {
                if (response.body().getStatus().equals("ok")) {
                    List<ArticlesItem> articlesItems = response.body().getArticles();
                    adapter = new CustomAdapter(rcNatgeo, MainActivity.this, articlesItems);
                    rcNatgeo.setAdapter(adapter);
                    rcNatgeo.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                }
            }

            @Override
            public void onFailure(Call<ResponseNatgeo> call, Throwable t) {

            }
        });
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

        Context context;
        List<ArticlesItem> articlesItems;

        public CustomAdapter(RecyclerView rcNatgeo, Context context, List<ArticlesItem> articlesItems) {

            this.context = context;
            this.articlesItems = articlesItems;

        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.natgeolist, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

            myViewHolder.txtPublish.setText(articlesItems.get(i).getPublishedAt());
            myViewHolder.txtTitle.setText(articlesItems.get(i).getTitle());
            myViewHolder.txtAuthor.setText(articlesItems.get(i).getAuthor());
            myViewHolder.txtDesc.setText(articlesItems.get(i).getDescription());
            Source source = (Source) articlesItems.get(i).getSource();
            myViewHolder.txtName.setText(source.getName());

            Glide.with(context)
                    .load(articlesItems.get(i).getUrlToImage())
                    .centerCrop()
                    .into(myViewHolder.image);

        }

        @Override
        public int getItemCount() {
            return articlesItems.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtName, txtTitle, txtAuthor, txtPublish, txtDesc;
            ImageView image;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                txtTitle = itemView.findViewById(R.id.txtTitle);
                txtName = itemView.findViewById(R.id.txtname);
                txtAuthor = itemView.findViewById(R.id.txtAuthor);
                txtPublish = itemView.findViewById(R.id.txtPublished);
                txtDesc = itemView.findViewById(R.id.txtDescription);
                image = itemView.findViewById(R.id.img);

            }
        }
    }
}

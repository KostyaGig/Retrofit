package com.kostya_zinoviev.retrofit.ex1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kostya_zinoviev.retrofit.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    //request с англ. - запрос
    //patch request c англ.- Запрос на исправление
    Button getButton,postButton,getByUserIdButton,patchRequestButton,deleteRequestButton;
    EditText edUserId;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.result);
        getButton = findViewById(R.id.get);
        postButton = findViewById(R.id.post);
        getByUserIdButton = findViewById(R.id.getByUserId);
        patchRequestButton = findViewById(R.id.patchRequest);
        deleteRequestButton = findViewById(R.id.deleteRequest);
        edUserId = findViewById(R.id.edUserId);

        //Create retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

         apiInterface = retrofit.create(ApiInterface.class);


        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResponse();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postData();
            }
        });

        //Получаем данные по id пользователя
        getByUserIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataByUserId();
            }
        });

        patchRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patchRequest();
            }
        });

        deleteRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRequest();
            }
        });
    }

    private void deleteRequest() {
        //Удаляем ответ по id
        Call<Void> call = apiInterface.deletePost(1);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                textView.setText("DEELTED " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                textView.setText("Error" + t.getMessage().toString());
            }
        });

    }

    private void patchRequest() {

        PostData postData = new PostData("this is Patch",null,1);

        Call<PostData> call = apiInterface.postData(postData);
        call.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {

                if (response.isSuccessful()){
                    //Если успешно,то получаем тело отправленной нами postData и устанавливаем эти данные текствью
                    PostData postData = response.body();

                    String responseCOde = String.valueOf(response.code());
                    String userId = String.valueOf(postData.getUserId());
                    String title = postData.getTitle();
                    String body = postData.getBody();

                    textView.append("Response code: " + responseCOde +"User id "+ userId +" title " + title +" body " + body);
                } else {
                    Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {
                Toast.makeText(MainActivity.this, "ERROR " + t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataByUserId() {
        Integer userId = Integer.parseInt(edUserId.getText().toString().trim());

        Call<List<GetPost>> getPostByUserId = apiInterface.getPostById(userId);
        getPostByUserId.enqueue(new Callback<List<GetPost>>() {
            @Override
            public void onResponse(Call<List<GetPost>> call, Response<List<GetPost>> response) {
                //Получаем уже list с пришедшими постами по userId
                List<GetPost> getPostByUserId = response.body();

                for (GetPost getCurrentPostByUserId:getPostByUserId){
                    String id = String.valueOf(getCurrentPostByUserId.getId());
                    String userId = String.valueOf(getCurrentPostByUserId.getUserId());
                    String title = getCurrentPostByUserId.getTitle();
                    String body = getCurrentPostByUserId.getBody();

                    textView.append("User id " + userId + " id " + id +" title " + title +" body " + body);
                }

            }

            @Override
            public void onFailure(Call<List<GetPost>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error" + t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void postData() {

        //Создаем экземпляр класса postData (нашу модель) и заполняем ее данными
        //Которые будем отправлять на севрер
        PostData postData = new PostData("New Data","Body data",101);

        Call<PostData> call = apiInterface.postData(postData);
        call.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {

                if (response.isSuccessful()){
                    //Если успешно,то получаем тело отправленной нами postData и устанавливаем эти данные текствью
                    PostData postData = response.body();

                    String responseCOde = String.valueOf(response.code());
                    String userId = String.valueOf(postData.getUserId());
                    String title = postData.getTitle();
                    String body = postData.getBody();

                    textView.append("Response code: " + responseCOde +"User id "+ userId +" title " + title +" body " + body);
                } else {
                    Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {
                Toast.makeText(MainActivity.this, "ERROR " + t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getResponse() {
        //Получаем в наш созданный listCall,те ,которые мы получим из сервера
        final Call<List<GetPost>> listCall = apiInterface.getAllPosts();

        //CallBack
        listCall.enqueue(new Callback<List<GetPost>>() {
            @Override
            public void onResponse(Call<List<GetPost>> call, Response<List<GetPost>> response) {
                //Метод вызывается при  ответе
                if (!response.isSuccessful()){
                    //Если не успешно
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Получаем уже все посты с нашего сервера,с помощью response.body()
                List<GetPost> getPosts = response.body();

                for (GetPost currentPosts:getPosts){
                    String userId = String.valueOf(currentPosts.getUserId());
                    String id = String.valueOf(currentPosts.getId());
                    String title = currentPosts.getTitle();
                    String body = currentPosts.getBody();

                    textView.append("User id " + userId + " id " + id +" title " + title +" body " + body);
                }

            }

            @Override
            public void onFailure(Call<List<GetPost>> call, Throwable t) {
                //Неудача
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
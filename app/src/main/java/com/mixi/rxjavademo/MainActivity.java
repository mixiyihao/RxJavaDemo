package com.mixi.rxjavademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mixi.rxjavademo.network.ApiService;
import com.mixi.rxjavademo.network.GetIpInfoResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private class User {
        User(int id, String userName, String dady, String mom) {
            this.id = id;
            this.userName = userName;
            this.userDady = dady;
            this.userMom = mom;
        }
        int id;
        String userName;
        String userDady;
        String userMom;
    }

    public void doCliclListener(){
        //myObserver.subscribe(mySubscriber);

        //简化版本
        Observable.just("hello world").subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("" + s);
            }
        });
        //map操作符
        Observable.just(" map -> hello world ").map(new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                return s.hashCode();
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("integer:" + integer);
            }
        });

        //一个简单的例子来描述Rxjava

        //1.领导要从数据库的用户表查出所有用户数据
        Observable.create(new Observable.OnSubscribe<List<User>>() {
            @Override
            public void call(Subscriber<? super List<User>> subscriber) {
                List<User> users = new ArrayList<User>();
                users.add(new User(1, "小明", "小東", "小紅"));
                users.add(new User(2, "小東", "小哥", "小話"));
                users.add(new User(3, "小哥", "小丑", "小黃"));
                //从数据库中获取数据并添加到users;
                subscriber.onNext(users);
            }
        }).subscribe(new Action1<List<User>>() {
            @Override
            public void call(List<User> users) {
                //获取到用户信息列表了
            }
        });
        //2. 领导个傻逼不要所有用户数据了 只要名字叫"小明"的用户
        Observable.create(new Observable.OnSubscribe<List<User>>() {
            @Override
            public void call(Subscriber<? super List<User>> subscriber) {
                List<User> users = new ArrayList<User>();
                users.add(new User(1, "小明", "小東", "小紅"));
                users.add(new User(2, "小東", "小哥", "小話"));
                users.add(new User(3, "小哥", "小丑", "小黃"));
                //从数据库中获取数据并添加到users;
                subscriber.onNext(users);
            }
        }).flatMap(new Func1<List<User>, Observable<User>>() {
            @Override
            public Observable<User> call(List<User> users) {
                return Observable.from(users);
            }
        }).filter(new Func1<User, Boolean>() {
            @Override
            public Boolean call(User user) {
                return user.userName.equals("小明");
            }
        }).subscribe(new Action1<User>() {
            @Override
            public void call(User user) {
                //拿到小明了
            }
        });
        //3.傻逼领导又说，不要小明了，我要小明的爸爸的数据
        Observable.create(new Observable.OnSubscribe<List<User>>() {
            @Override
            public void call(Subscriber<? super List<User>> subscriber) {
                List<User> users = new ArrayList<User>();
                users.add(new User(1, "小明", "小東", "小紅"));
                users.add(new User(2, "小東", "小哥", "小話"));
                users.add(new User(3, "小哥", "小丑", "小黃"));
                //从数据库中获取数据并添加到users;
                subscriber.onNext(users);
            }
        }).flatMap(new Func1<List<User>, Observable<User>>() {
            @Override
            public Observable<User> call(List<User> users) {
                return Observable.from(users);
            }
        }).filter(new Func1<User, Boolean>() {
            @Override
            public Boolean call(User user) {
                return user.userName.equals("小明");
            }
        }).map(new Func1<User, User>() {

            @Override
            public User call(User user) {
                //从数据库中获取小明的爸爸
                return new User(3, "小哥", "小丑", "小黃");
            }
        }).subscribe(new Action1<User>() {
            @Override
            public void call(User user) {
                //拿到小明的爸爸了
            }
        });

        //4.上面的都是在當前線程的实现的
        Subscription mixi = Observable.just(1, 2, 3, 4, 5, 6, 7, 8)
                .subscribeOn(Schedulers.io()) //指定在I/O线程
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e("mixi", integer + "---");
                    }
                });


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.chufa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //doCliclListener();
                useRetrofit();
            }
        });

    }

    /**
     * 使用retrofit + rxandroid 获取json数据
     */
    public void useRetrofit(){
        String baseUrl="http://192.168.6.253:8080/RetrofitDemo/";
        String phoneNumber="http://apis.juhe.cn/mobile/get?phone=18508213050&key=";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<GetIpInfoResponse> userInfo = apiService.getUserInfo();
        userInfo.enqueue(new Callback<GetIpInfoResponse>() {
            @Override
            public void onResponse(Call<GetIpInfoResponse> call, Response<GetIpInfoResponse> response) {
                if(response.isSuccessful()){
                    GetIpInfoResponse body = response.body();
                    Log.e("mixi","info="+body);
                }else{
                    Log.e("mixi",response.errorBody().toString());
                }


            }

            @Override
            public void onFailure(Call<GetIpInfoResponse> call, Throwable t) {
                Log.e("mixi","reqeust failer");
            }
        });

    }

    public void useRetrofitAndRxAndroid(){
        String baseUrl="http://192.168.6.253:8080/RetrofitDemo/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService.getUserInfo2().subscribe(new Subscriber<GetIpInfoResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GetIpInfoResponse getIpInfoResponse) {
                Log.e("mixi",getIpInfoResponse.toString());
            }
        });
    }




}

       /* myObserver = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.e("mixi","call");
                subscriber.onCompleted();
            }
        });

        mySubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e("mixi","onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("mixi","onError");
            }

            @Override
            public void onNext(String s) {
                Log.e("mixi","onNext");
            }
        };*/





/*

    public Bitmap getBitmapFromFile(File file){
        return null;
    }


     * 接下来就是使用RxJava
     *
     *
     *
     * @param folders
     *//*

    private Subscriber<String> subsriber = new Subscriber<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String s) {

        }
    };

    public void useRxJava(File[] folders) {
        Observable.from(folders)
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        return Observable.from(file.listFiles());
                    }
                })
                .filter(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        return file.getName().endsWith(".png");
                    }
                })
                .map(new Func1<File, Bitmap>() {
                    @Override
                    public Bitmap call(File file) {
                        return getBitmapFromFile(file);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        //TODO imageCollectorView.addImage(bitmap);
                    }
                });

    }

    public interface GithubService{
        @GET("users/{user}/repos")
        Call<List<String>> listRepose(@Path("user") String user);
    }
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/").build();
    GithubService service = retrofit.create(GithubService.class);


*/



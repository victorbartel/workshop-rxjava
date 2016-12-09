import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.util.JSONPObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable
import rx.schedulers.Schedulers

// Errors handling

class Step3 {

    static main(args) {
        getWithException()
        //getWithoutException()
        sleep(3000)
    }

    private static getWithException() {
        def final swapiService = getSwapiService()
        getObservable()
            .flatMap({ swapiService.getPlanet(it) })
            .map({ it.at('/name').textValue() })
            .subscribe({ println it })
    }

    private static getObservable() {
        Observable.just(1, Integer.MAX_VALUE)
            .subscribeOn(Schedulers.io())
    }

    private static getWithoutException() {
        def final swapiService = getSwapiService()
        getObservable()
            .flatMap({
                swapiService.getPlanet(it)
                    .retry(3)
                    .onErrorResumeNext({ex -> println "[ERROR] Oh my goodness: ${ex.message}"; Observable.empty() })
            })
            .map({ it.at('/name').textValue() })
            .subscribe({ println it })
    }

    static def getSwapiService() {
        def retrofit = new Retrofit.Builder()
            .baseUrl("http://swapi.co/api/")
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

        retrofit.create(SwapiService.class);
    }
    static interface SwapiService {
        @GET("planets/{id}")
        Observable<JsonNode> getPlanet(@Path("id") int id)
    }
}


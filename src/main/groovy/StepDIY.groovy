import com.fasterxml.jackson.databind.JsonNode
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable


class StepDIY {

    static main(args) {
        // Write a method that will list all planets alongside films where this planet appears
        // Add error handling with logging and retry
        // [Optional] Add a chain for displaying planets -> films -> starships information
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
        @GET("planets")
        Observable<JsonNode> getPlanets()

        @GET("films/{id}")
        Observable<JsonNode> getFilms(@Path("id") int id)
    }
}

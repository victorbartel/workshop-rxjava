import com.fasterxml.jackson.databind.JsonNode
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

import static java.util.stream.Collectors.toList

// Super mega hyper awesome example

class Step4 {

    static main(args) {

        def final swapiService = getSwapiService()
        wrapRestCall(swapiService.planets)
            .map({ it.at("/results").asCollection() })
            .flatMapIterable({ it })
            .filter({ it.at("/residents").size() > 0 })
            .map({ getPairsPlanetCharacterId(it) })
            .flatMapIterable({it})
            .flatMap({ wrapRestCall(swapiService.getCharacter(it.character)) }, getFilmsFromCharacter)
            .flatMapIterable({ it })
            .flatMap({ wrapRestCall(swapiService.getFilms(it.film))},
                     { map, JsonNode node -> map << [film : node.at("/title")] ; map })
            .subscribe({ println "The character ${it.character} from ${it.planet} appears in ${it.film}" })
    }

    static final wrapRestCall (Observable<JsonNode> observable) {
        observable.retry(2).onErrorResumeNext({ println "[WARN] {it.message}" })
    }
    def final static getFilmsFromCharacter = { map, JsonNode node ->
        node.at("/films").asCollection().stream().map({
            [
                planet   : map.planet,
                character: node.at("/name").asText(),
                film     : Integer.parseInt(it.asText().replaceAll("\\D+", ''))
            ]
        }).collect(toList())
    }


    private static getPairsPlanetCharacterId(JsonNode node) {
        node.at("/residents")
            .asCollection()
            .stream().map({ [ planet: "${node.at('/name').textValue()}",
                              character : Integer.parseInt(it.textValue().replaceAll("\\D+", ''))] })
            .collect(toList())
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

        @GET("people/{id}")
        Observable<JsonNode> getCharacter(@Path("id") int id)

        @GET("films/{id}")
        Observable<JsonNode> getFilms(@Path("id") int id)
    }
}
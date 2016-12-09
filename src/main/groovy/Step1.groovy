import rx.Observable
import rx.Subscriber


// Very simple examples

class Step1 {
    static main(args) {

        // Create an observable
        def final observable = Observable.just("Hi there");

        // Subscribe to the observable
        observable.subscribe({ println it })

        //transform()
        //transformList()
    }

    private static transform() {

        def final observable = Observable.just("Hi there", "Bonjour", "Seid gegrüßt");

        // Not a good approach
        observable.subscribe({ println it + ' $USERNAME'})

        // Better way with operators
        observable.map({it + ' $USERNAME'}).subscribe({ println it })
    }

    private static transformList() {
        final def listsOfObservable = Observable.just(["Hi there", "Yo bro", "Greetings", "Good morning"],
                                                      ["Bonjour", "Salut"],
                                                      ["Seid gegrüßt", "Hallo"]);
        listsOfObservable
            .flatMapIterable({ it })
            .filter({ it.matches('^(?:Yo|Ha|Sa).*') })
            .map({ it + ' $USERNAME' })
            .subscribe({ println it })
    }
}

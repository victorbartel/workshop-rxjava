import rx.Observable
import rx.Scheduler
import rx.Subscriber
import rx.schedulers.Schedulers

import java.util.concurrent.Executors

// Useful reactivity
class Step2 {

    static main(args) {
        final Observable<String> observable = Observable.fromCallable({
            println "From observable: ${Thread.currentThread().name}"
            [1, 2, 3, 4]
        }).flatMapIterable({
            println "From flatMapIterable operator: ${Thread.currentThread().name}"
            it
        }).map({
            println "From map operator: ${Thread.currentThread().name}"
            it.toString()
        })
        // observeOn - helps to change the scheduler in the middle of the process

        wrapToNothing(observable).subscribe({ println "From subscriber: ${Thread.currentThread().name} : $it" })
        //wrapToComputation(observable).subscribe({ println "From subscriber: ${Thread.currentThread().name} : $it" })
        //wrapToNewThread(observable).subscribe({ println "From subscriber: ${Thread.currentThread().name} : $it" })
        //wrapToIO(observable).subscribe({ println "From subscriber: ${Thread.currentThread().name} : $it" })
        sleep(3000)
    }

    private static wrapToNothing(Observable<String> observable) {
        observable
    }

    private static wrapToComputation(Observable<String> observable) {
        observable
            .subscribeOn(Schedulers.computation())
    }

    private static wrapToNewThread(Observable<String> observable) {
        observable
            .subscribeOn(Schedulers.newThread())
    }

    private static wrapToIO(Observable<String> observable) {
        observable
            .subscribeOn(Schedulers.io())
    }
}

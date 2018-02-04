package com.debugg3r.android.solarwallpaper;

import org.junit.Test;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.Subscription;
import rx.subjects.BehaviorSubject;

public class RxTest {

    @Test
    public void testSingle() {
        BehaviorSubject<Integer> subject = BehaviorSubject.create();

        subject.onNext(1);

        subject.subscribe(
                integer -> System.out.println("sub1: Integer: " + integer),
                throwable -> System.out.println("sub1: error"),
                () -> System.out.println("sub1: onComplete"));

        subject.onNext(2);

        Subscription subscription = subject.subscribe(
                integer -> System.out.println("sub2: Integer: " + integer),
                throwable -> System.out.println("sub2: error"),
                () -> System.out.println("sub2: onComplete"));

        subject.onNext(3);

        subscription.unsubscribe();

        subject.onNext(4);

        subject.onCompleted();
    }
}

package ru.geekbrains.sklyarov.race;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class MainClass {

    // Количество участников
    public static final int CARS_COUNT = 4;

    // Барьер для старта
    private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(CARS_COUNT + 1);


    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), cyclicBarrier);
        }

        for (Car car : cars) {
            new Thread(car).start();
        }

        cyclicBarrier.await();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        cyclicBarrier.await();

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}

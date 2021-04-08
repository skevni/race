package ru.geekbrains.sklyarov.race;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage{
    private final Semaphore semaphore = new Semaphore(MainClass.CARS_COUNT / 2);
    public Tunnel() {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
    }
    @Override
    public void go(Car c) {
        try {
            try {
                System.err.println(c.getName() + " готовится к этапу(ждет): " + description);
                semaphore.acquire();
                System.err.println(c.getName() + " начал этап: " + description );
                Thread.sleep(length / c.getSpeed() * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.err.println(c.getName() + " закончил этап: " + description);
                semaphore.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

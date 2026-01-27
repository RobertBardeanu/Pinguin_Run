/**
 * Game Loop Verwalter für konstante fps
 * @author Robert, Maik, Tobias
 * @version 1.0
 */
public class GameLoop implements Runnable
{
    boolean gameRunning;

    final static long TARGET_FPS_NS = 1_000_000_000L / 60;

    public Thread gameThread;


    final AppWindow appWindowRef;

    public GameLoop(final AppWindow appWindowRef)
    {
        this.appWindowRef = appWindowRef;
    }

    /**
     * Gamerun Start
     */
    public synchronized void start()
    {
        if (gameRunning) return;

        gameRunning = true;
        gameThread = new Thread(this);
        gameThread.start();

    }

    /**
     * Stoppen
     */
    public synchronized void stop()
    {
        gameRunning = false;
        try
        {
            gameThread.join();
        }
        catch (InterruptedException e) {}
    }

    /**
     * Fps Checker und konstant halten
     */
    @Override
    public void run()
    {
        long lastTimeNS = System.nanoTime();

        while (gameRunning)
        {
            final long nowNS  = System.nanoTime();
            final long elapsedNanos = nowNS - lastTimeNS;
            lastTimeNS = nowNS;

            final double deltaTimeSeconds = elapsedNanos / 1e9;

            updateAll(deltaTimeSeconds);

            final long frameTime = System.nanoTime() - nowNS;
            final long sleepTime = TARGET_FPS_NS - frameTime;

            if (sleepTime > 0)
            {
                try
                {
                    Thread.sleep(sleepTime / 1_000_000L, (int) (sleepTime % 1_000_000L));
                }
                catch (InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                    break;
                }

                continue;
            }

            try
            {
                Thread.sleep(1);
            }
            catch (InterruptedException ex)
            {
                Thread.currentThread().interrupt();
                break;
            }

        }
    }

    /**
     * App Window Updaten
     * @param deltaTime
     */
    public void updateAll(final double deltaTime)
    {
        //System.out.println("DT: " + deltaTime);
        appWindowRef.updateAll(deltaTime);
    }
}

package org.aksw.verilinks.games.peaInvasion.client.core.info;
import static playn.core.PlayN.currentTime;

public class FpsCounter {
  
  private static final int FPS_COUNTER_MAX = 300;

  private int frameCounter = 0;
  private double frameCounterStart = 0;
  
  private final InfoText info;

  public FpsCounter(InfoText info) {
      this.info = info;
  }

  public void update() {
      if (frameCounter == 0) {
          frameCounterStart = currentTime();
      }

      frameCounter++;
      if (frameCounter == FPS_COUNTER_MAX) {
          int frameRate = (int) (frameCounter / ((currentTime() - frameCounterStart) / 1000.0));
          info.updateFrameRate(frameRate);
          frameCounter = 0;
      }
  }
  
}
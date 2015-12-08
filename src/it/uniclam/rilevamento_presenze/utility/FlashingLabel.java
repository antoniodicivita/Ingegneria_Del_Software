package it.uniclam.rilevamento_presenze.utility;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Created by Chriz 7X on 07/12/2015.
 */
public class FlashingLabel extends Label
{
    private FadeTransition animation;

    public FlashingLabel()
    {
        animation = new FadeTransition(Duration.millis(1000), this);
        animation.setFromValue(1.0);
        animation.setToValue(0);
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.setAutoReverse(true);
        animation.play();

        visibleProperty().addListener(new ChangeListener<Boolean>()
        {
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue)
            {
                if (newValue)
                {
                    animation.playFromStart();
                }
                else
                {
                    animation.stop();
                }
            }
        });
    }
}
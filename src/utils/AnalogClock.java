/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Ahmed
 */
import java.util.Calendar;
import java.util.GregorianCalendar;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class AnalogClock extends Group {

    public AnalogClock() {
        
        final Circle face = new Circle(100, 100, 100);
        face.setId("face");

        final Line hourHand = new Line(0, 0, 0, -50);
        hourHand.setTranslateX(100);
        hourHand.setTranslateY(100);
        hourHand.setId("hourHand");

        final Line minuteHand = new Line(0, 0, 0, -75);
        minuteHand.setTranslateX(100);
        minuteHand.setTranslateY(100);
        minuteHand.setId("minuteHand");

        final Line secondHand = new Line(0, 15, 0, -88);
        secondHand.setTranslateX(100);
        secondHand.setTranslateY(100);
        secondHand.setId("secondHand");

        final Circle spindle = new Circle(100, 100, 5);
        spindle.setId("spindle");

        Group ticks = new Group();
        for (int i = 0; i < 12; i++) {
            Line tick = new Line(0, -83, 0, -93);
            tick.setTranslateX(100);
            tick.setTranslateY(100);
            tick.getStyleClass().add("tick");
            tick.getTransforms().add(new Rotate(i * (360 / 12)));
            ticks.getChildren().add(tick);
        }

        this.getChildren().addAll(face, ticks, spindle, hourHand, minuteHand, secondHand);

        // determine the starting time.
        Calendar calendar = GregorianCalendar.getInstance();
        final double seedSecondDegrees = calendar.get(Calendar.SECOND) * (360 / 60);
        final double seedMinuteDegrees = (calendar.get(Calendar.MINUTE) + seedSecondDegrees / 360.0) * (360 / 60);
        final double seedHourDegrees = (calendar.get(Calendar.HOUR) + seedMinuteDegrees / 360.0) * (360 / 12);

        // define rotations to map the analogueClock to the current time.
        final Rotate hourRotate = new Rotate(seedHourDegrees);
        final Rotate minuteRotate = new Rotate(seedMinuteDegrees);
        final Rotate secondRotate = new Rotate(seedSecondDegrees);
        hourHand.getTransforms().add(hourRotate);
        minuteHand.getTransforms().add(minuteRotate);
        secondHand.getTransforms().add(secondRotate);

        // the hour hand rotates twice a day.
        final Timeline hourTime = new Timeline(
                new KeyFrame(
                        Duration.hours(12),
                        new KeyValue(
                                hourRotate.angleProperty(),
                                360 + seedHourDegrees,
                                Interpolator.LINEAR
                        )
                )
        );

        // the minute hand rotates once an hour.
        final Timeline minuteTime = new Timeline(
                new KeyFrame(
                        Duration.minutes(60),
                        new KeyValue(
                                minuteRotate.angleProperty(),
                                360 + seedMinuteDegrees,
                                Interpolator.LINEAR
                        )
                )
        );

        // move second hand rotates once a minute.
        final Timeline secondTime = new Timeline(
                new KeyFrame(
                        Duration.seconds(60),
                        new KeyValue(
                                secondRotate.angleProperty(),
                                360 + seedSecondDegrees,
                                Interpolator.LINEAR
                        )
                )
        );

        // time never ends.
        hourTime.setCycleCount(Animation.INDEFINITE);
        minuteTime.setCycleCount(Animation.INDEFINITE);
        secondTime.setCycleCount(Animation.INDEFINITE);

        // start the analogueClock.
        secondTime.play();
        minuteTime.play();
        hourTime.play();
    }
}
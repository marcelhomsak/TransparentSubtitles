module com.marcelhomsak.transparentsubtitles {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.marcelhomsak.transparentsubtitles to javafx.fxml;
    exports com.marcelhomsak.transparentsubtitles;
}
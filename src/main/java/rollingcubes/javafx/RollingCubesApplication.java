package rollingcubes.javafx;

import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.AbstractModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;
import rollingcubes.results.GameResultRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

public class RollingCubesApplication extends Application {
    private final GuiceContext context = new GuiceContext(this, () -> List.of(
            new AbstractModule() {
                @Override
                protected void configure() {
                    bind(GameResultRepository.class).in(Singleton.class);
                }
            }
    ));

    @Inject
    private FXMLLoader fxmlLoader;

    @Override
    public void start(Stage stage) throws Exception {
        Logger.info("Starting application");
        context.init();
        fxmlLoader.setLocation(getClass().getResource("/fxml/opening.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("Rolling Cubes");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }

}

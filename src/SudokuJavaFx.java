import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SudokuJavaFx extends Application {

	private enum coord {NORTH, EAST};

    private static final int TILE_SIZE = 40;

    private static final int X_TILES = 9;
    private static final int Y_TILES = 9;
    private static final int BORDER_SIZE = 1;
    private static final int BONUS_BORDER = 2;
    private static final int W = X_TILES*TILE_SIZE + BORDER_SIZE + 3*BONUS_BORDER;
    private static final int H = Y_TILES*TILE_SIZE + BORDER_SIZE + 3*BONUS_BORDER;


    private Tile[][] grid = new Tile[X_TILES][Y_TILES];
    private Scene scene;

    private File file;

    private Tile currentTile;

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(W, H);
        root.setStyle("-fx-background-color: black;");

        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Tile tile = new Tile(x, y);

                grid[x][y] = tile;
                root.getChildren().add(tile);
            }
        }

        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Tile tile = grid[x][y];

            }
        }

        currentTile = grid[0][5];

        currentTile.setSelected(true);
        currentTile.setTileText("3");

        return root;
    }


    private List<Tile> getNeighbors(Tile tile) {
        List<Tile> neighbors = new ArrayList<>();

        // ttt
        // tXt
        // ttt

        int[] points = new int[] {
              -1, -1,
              -1, 0,
              -1, 1,
              0, -1,
              0, 1,
              1, -1,
              1, 0,
              1, 1
        };

        for (int i = 0; i < points.length; i++) {
            int dx = points[i];
            int dy = points[++i];

            int newX = tile.x + dx;
            int newY = tile.y + dy;

            if (newX >= 0 && newX < X_TILES
                    && newY >= 0 && newY < Y_TILES) {
                neighbors.add(grid[newX][newY]);
            }
        }

        return neighbors;
    }

    private class Tile extends StackPane {
        private int x, y;
        private boolean selected = false;

        private Rectangle cell = new Rectangle(TILE_SIZE - 2*BORDER_SIZE, TILE_SIZE - 2*BORDER_SIZE);
        private Rectangle border = new Rectangle(TILE_SIZE, TILE_SIZE);
        private Text text = new Text();

        public Tile(int x, int y) {
            this.x = x;
            this.y = y;

            border.setStroke(Color.BLUE);
            border.setFill(Color.BLUE);
            cell.setStroke(Color.LIGHTGRAY);
            cell.setFill(Color.WHITE);

            text.setFont(Font.font(18));
            text.setText("0");

            border.setVisible((selected ? true : false));

            getChildren().addAll(border, cell, text);

            setTranslateX(x * TILE_SIZE + BORDER_SIZE + BONUS_BORDER*(x/3));
            setTranslateY(y * TILE_SIZE + BORDER_SIZE + BONUS_BORDER*(y/3));
        }

        public void setSelected(boolean bool){
        	selected = bool;

        	border.setVisible(bool);
        	if(bool){ //Decrease the white cell size, to increase the selected border size
        		cell.setWidth(TILE_SIZE - 6*BORDER_SIZE);
        		cell.setHeight(TILE_SIZE - 6*BORDER_SIZE);
        	} else { //Increase the white cell size, to revert the border size
        		cell.setWidth(TILE_SIZE - 2*BORDER_SIZE);
        		cell.setHeight(TILE_SIZE - 2*BORDER_SIZE);
        	}
        }

        public void setTileText(String newText){
        	text.setText(newText);
        }

    }

    private void move(Point moveCoord) {
    	int newX = currentTile.x + moveCoord.x;
    	int newY = currentTile.y + moveCoord.y;

    	if((newX < 0 || newX > 8) || (newY < 0 || newY > 8)){
    		System.out.println("well fuck");
    		return;
    	}

    	currentTile.setSelected(false);
    	currentTile = grid[newX][newY];
    	currentTile.setSelected(true);

	}

    private void insert(char number){
    	currentTile.setTileText(Character.toString(number));
    }


    public void handleCommand(String command){
    	System.out.println(command);
    	switch(command.charAt(0)){
    	case 'M':
    		switch(command.charAt(1)){
    		case 'U':
    			move(new Point(0, -1));
    			break;
    		case 'R':
    			move(new Point(1, 0));
    			break;
    		case 'D':
    			move(new Point(0, 1));
    			break;
    		case 'L':
    			move(new Point(-1, 0));
    			break;
    		}
    		break;
    	case 'I':
    		insert(command.charAt(1));
    		break;
    	}
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(createContent());

        stage.setScene(scene);
        stage.show();

        FileListener fl = new FileListener();
        fl.setApp(this);
        fl.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

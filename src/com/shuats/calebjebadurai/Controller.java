package com.shuats.calebjebadurai;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
	private static int COLUMNS;
	private static int ROWS;
	private static double INPUT_DATA[][];
	private static double MEAN[];
	private static double DEVIATION_DATA[][];
	private static double TRANSPOSED_DATA[][];
	private static double COVARIANCE_DATA[][];
	private static String FILE_PATH;
	private static final int CIRCLE_DIAMETER = 120;
	private static final String DISC_COLOR1 = "#24303E";
	private static final String DISC_COLOR2 = "#4CAA88";

	private static String playerOne = "Player One";
	private static String playerTwo = "Player Two";

	private boolean isPlayerOneTurn = true;
	private boolean  isAllowedToInsert = true;

//	private Disc[][] insertedDiscsArray = new Disc[ROWS][COLUMNS];

	@FXML
	public GridPane rootGridPane;

	@FXML
	public Pane insertedDiscPane, playerDetails;

	@FXML
	public Label playerNameLabel;

	@FXML
	public TextField playerOneName, playerTwoName;

	@FXML
	public Button submitButton;

	public void createPlayground() {

		Rectangle bigRectangle1 = new Rectangle(CIRCLE_DIAMETER * 3, CIRCLE_DIAMETER);
		Rectangle bigRectangle2 = new Rectangle(CIRCLE_DIAMETER * 3, CIRCLE_DIAMETER);
		Rectangle bigRectangle3 = new Rectangle(CIRCLE_DIAMETER * 3, CIRCLE_DIAMETER);
		Rectangle bigRectangle4 = new Rectangle(CIRCLE_DIAMETER * 3, CIRCLE_DIAMETER);
		Rectangle bigRectangle5 = new Rectangle(CIRCLE_DIAMETER * 3, CIRCLE_DIAMETER);
		Rectangle bigRectangle6 = new Rectangle(CIRCLE_DIAMETER * 3, CIRCLE_DIAMETER);
		Rectangle bigRectangle7 = new Rectangle(CIRCLE_DIAMETER * 3, CIRCLE_DIAMETER);
		Rectangle bigRectangle8 = new Rectangle(CIRCLE_DIAMETER * 3, CIRCLE_DIAMETER);
		Rectangle smallRectangle = new Rectangle(CIRCLE_DIAMETER * 3, CIRCLE_DIAMETER);

		smallRectangle.setFill(Color.RED);

		smallRectangle.setOnMouseEntered(event -> smallRectangle.setFill(Color.valueOf("bbbbbb22")));
		smallRectangle.setOnMouseExited(event -> smallRectangle.setFill(Color.TRANSPARENT));

		rootGridPane.add(bigRectangle1,0,1);
		rootGridPane.add(bigRectangle2,1,1);
		rootGridPane.add(bigRectangle3,2,1);

		rootGridPane.add(bigRectangle4,0,2);
		rootGridPane.add(smallRectangle,1,2);
		rootGridPane.add(bigRectangle5,2,2);

		rootGridPane.add(bigRectangle6,0,3);
		rootGridPane.add(bigRectangle7,1,3);
		rootGridPane.add(bigRectangle8,2,3);

		smallRectangle.setOnMouseClicked(event -> {
			FILE_PATH = loadNewData(new Stage());
			if (FILE_PATH != "File Not Found") {
				setRowCol(FILE_PATH);

				INPUT_DATA = setData();

				DEVIATION_DATA = getDeviationData(INPUT_DATA);

				TRANSPOSED_DATA = getTransposedMatrix(DEVIATION_DATA);

				COVARIANCE_DATA = covMat(DEVIATION_DATA);

				System.out.println("Input data");
				displayMatrix(INPUT_DATA);
				System.out.println("Mean");
				displayMatrix(MEAN);
				System.out.println("Deviation");
				displayMatrix(DEVIATION_DATA);
				System.out.println("Transposed");
				displayMatrix(TRANSPOSED_DATA);
				System.out.println("Covariance");
				displayMatrix(COVARIANCE_DATA);

			}
		});

	}
//		Shape rectangleWithHoles = createGameStructuralGrid();
//		rootGridPane.add(rectangleWithHoles,0,1);
//
//		List<Rectangle> rectangleList = createClickableRectangle();
//		for (Rectangle rectangle: rectangleList) {
//
//			rootGridPane.add(rectangle, 0,1);
//		}
//
//		submitButton.setOnAction(event -> {
//			playerOne = playerOneName.getText();
//			playerTwo = playerTwoName.getText();
//		});
//	}

	private String loadNewData(Stage primaryStage) {

		primaryStage.setTitle("Import Data from Text file");

		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new java.io.File("/"));
		fileChooser.setTitle("Select Only a (*.txt) file.");

		// Set extension filter
		FileChooser.ExtensionFilter extFilter =
				new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		File file = fileChooser.showOpenDialog(primaryStage);
		if (file != null) {
			return file.getPath();
		}
		else return "File Not Found";
	}

	private void setRowCol(String filepath)//sets row, column and instantiates rawData
	{
		String stringLine;
		StringTokenizer tokens;
		int row = 0, col = 0;
		try
		{

			FileReader fileReader = new FileReader(filepath);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((stringLine = bufferedReader.readLine()) != null)
			{
				if(row==0)
				{
					tokens=new StringTokenizer(stringLine);
					col=tokens.countTokens();
				}
				row++;
			}
			ROWS = row;
			COLUMNS = col;
			bufferedReader.close();
			fileReader.close();
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		INPUT_DATA=new double[ROWS][COLUMNS];
		MEAN = new double [COLUMNS];

		System.out.println("check 1\t"+row+"\t"+col);
	}

	private double[][] setData() //throws IOException
	{
		StringTokenizer st;
		int R=0,C;
		double X[][] = new double[ROWS][COLUMNS];
		try
		{
			FileReader fr = new FileReader(FILE_PATH);
			BufferedReader br = new BufferedReader(fr);
			String s;

			while((s=br.readLine())!= null && R<ROWS)
			{
				st=new StringTokenizer(s);
				//double sum=0;
				for(C=0;C<COLUMNS;C++)
				{
					try
					{
						String temp=st.nextToken();
						//System.out.print("    :"+temp+":");
						double positive= Double.parseDouble(temp);
//						if (positive<0)positive*=-1;
						X[R][C]=positive;
						MEAN[C] += positive;
					}
					catch(Exception e)
					{
						System.out.print("Null Pointer"+e);
					}
					//sum+=RAW_DATA[R][C];
				}
				System.out.println("");
				//RAW_DATA[R][0]=sum;
				R++;
			}

//			 TO find the Mean of the Sum stored in matrix MEAN
			for(int i=0; i<MEAN.length;i++)
			{
				MEAN[i]/=R;
			}

			br.close();
			fr.close();
		}
		catch(Exception e) {
			System.out.println("IOE" + e);
		}

		return X;
	}

	private double[][] getDeviationData(double[][] X){
		double Y[][] = new double[X.length][X[1].length];
		System.out.println("Normalizing data. started.");
		for(int i=0; i<Y.length; i++)
		{
			for(int j=0; j<Y[i].length; j++){
				Y[i][j] = X[i][j] - MEAN[j];
			}
			System.out.println("");
		}
		return Y;
	}


	private double[][] getTransposedMatrix(double X[][]){

		System.out.println("I am Here in Transpose matrix it's Started");

		int yr = X[1].length;
		int yc = X.length;

		final double[][] Y = new double[yr][yc];

		System.out.println("Y Rows="+yr+"; Columns="+yc);

		for(int i=0; i<yr; i++)
		{
			for(int j=0; j<yc; j++){
				Y[i][j]=X[j][i];
			}
		}

		System.out.println("I am Here in Transpose matrix: It's finished");

		return Y;
	}

//	private double[][] cross(){
//return
//	}

	private double[][] covMat(double X[][]){
		int yRow = X[1].length;
		double Y[][] = new double[yRow][yRow];

		for(int i = 0; i < yRow; i++)
		{
			for(int j = 0; j < yRow; j++)
			{
				int k;
				Y[i][j] = 0;
				for(k = 0; k < X.length; k++)
				{
					Y[i][j] += X[k][i] * X[k][j];
					System.out.println("\ni:"+i+"\nj:"+j+"\nk:"+k+"\nY["+i+"]["+j+"]:"+Y[i][j]+"\n");
				}
				Y[i][j] /= k;
			}
		}
		return Y;
	}

//	private double[][] covarianceMatrix(double X[][]){
//
//		System.out.println("Covariance Step 1:");
//		int yRow = (X.length<X[1].length)?X.length:X[1].length;
//		double Y[][] = new double[yRow][yRow];
//
//		System.out.println("Covariance Step 2:");
//		for(int i=0; i<yRow; i++)
//		{
//			for (int j=0; j<yRow; j++)
//				Y[i][j] = multiplicativeAddition(X[j],X[i]);
//		}
//		System.out.println("Covariance Step 3:");
//		return Y;
//	}
//
//	private double multiplicativeAddition(double[] A, double[] B) {
//		double sum = 0;
//		int i;
//
//		System.out.println("multi Add Step 1:");
//		for(i=0; i<A.length; i++)
//			sum+= A[i]*B[i];
//		System.out.println("multi Add Step 2:");
//		return (sum);
//	}

	private void displayMatrix(double[][] X){
		System.out.println("Displaying the Matrix");
		for(int i=0; i<X.length; i++)
		{
			for(int j=0; j<X[i].length; j++){
				System.out.print("\t\t"+X[i][j]);
			}
			System.out.println("");
		}
	}

	private void displayMatrix(double[] X){
		System.out.println("Displaying the Array");
		for(int i=0; i<X.length; i++)
		{
			System.out.println("\t\t"+X[i]);
		}
	}










//	public Shape createGameStructuralGrid() {
//
//		Shape rectangleWithHoles = new Rectangle((COLUMNS+1) * CIRCLE_DIAMETER, (ROWS+1) * CIRCLE_DIAMETER);
//
//		for (int row = 0; row < ROWS; row++) {
//			for (int col = 0; col < COLUMNS; col++) {
//
//				Circle circle = new Circle();
//				circle.setRadius(CIRCLE_DIAMETER / 2);
//				circle.setCenterX(CIRCLE_DIAMETER / 2);
//				circle.setCenterY(CIRCLE_DIAMETER / 2);
//
//				circle.setTranslateX(col * (CIRCLE_DIAMETER + 5)+(CIRCLE_DIAMETER / 4));
//				circle.setTranslateY(row * (CIRCLE_DIAMETER + 5)+(CIRCLE_DIAMETER / 4));
//
//				rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
//			}
//
//		}
//		rectangleWithHoles.setFill(Color.WHITE);
//
//		return rectangleWithHoles;
//	}

//	private Rectangle createClickableRectangle() {}

//		List<Rectangle> rectangleList = new ArrayList<>();

//		for (int col = 0; col < COLUMNS ; col++) {
//			Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER * 3, CIRCLE_DIAMETER);
//			rectangle.setTranslateX(col * (CIRCLE_DIAMETER + 5) + (CIRCLE_DIAMETER/4));
//			rectangle.setFill(Color.RED);
//
//			rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("bbbbbb22")));
//			rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));

//			final int column = col;
//			rectangle.setOnMouseClicked(event -> {
//				if (isAllowedToInsert){
//					isAllowedToInsert = false;
//					insertDisc(new Disc(isPlayerOneTurn), column);
//				}
//			});

//			rectangleList.add(rectangle);
//		}
//		return rectangleList;
//		return rectangle;
//	}

//	private void insertDisc(Disc disc, int column) {
//
//		int row = ROWS - 1;
//
//		while(row > 0) {
//			if(insertedDiscsArray[row][column] == null) break;
//			row--;
//		}
//
//		if(row < 0) return;
//
//		insertedDiscsArray[row][column]=disc;
//		insertedDiscPane.getChildren().addAll(disc);
//
//		disc.setTranslateX(column * (CIRCLE_DIAMETER + 5) + (CIRCLE_DIAMETER / 4));
//
//		int currentRow = row;
//		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5),disc);
//		translateTransition.setToY(row * (CIRCLE_DIAMETER + 5) + (CIRCLE_DIAMETER / 4));
//		translateTransition.setOnFinished(event -> {
//
//			isAllowedToInsert = true;
//			if(gameEnded(currentRow, column))gameOver();
//
//			isPlayerOneTurn = !isPlayerOneTurn;
//			playerNameLabel.setText(isPlayerOneTurn? playerOne: playerTwo);
//		});
//
//		translateTransition.play();
//	}

//	private boolean gameEnded(int row, int column) {
//
//		//Vertical Points
//		List<Point2D> verticalPoints = IntStream.rangeClosed(row - 3,row + 3)
//				.mapToObj(r -> new Point2D(r,column))
//				.collect(Collectors.toList());
//
//		//Horizontal Points
//		List<Point2D> horizontalPoints = IntStream.rangeClosed(column - 3, column + 3)
//				.mapToObj(col -> new Point2D(row, col))
//				.collect(Collectors.toList());
//
//		//Diagonal 1
//		Point2D startPoint1 = new Point2D(row - 3, column + 3);
//		List<Point2D> diagonal1Points = IntStream.rangeClosed(0, 6)
//				.mapToObj(i -> startPoint1.add(i, -i))
//				.collect(Collectors.toList());
//
//		//Diagonal 2
//		Point2D startPoint2 = new Point2D(row - 3, column - 3);
//		List<Point2D> diagonal2Points = IntStream.rangeClosed(0, 6)
//				.mapToObj(i -> startPoint2.add(i, i))
//				.collect(Collectors.toList());
//
//		boolean isEnded = checkCombinations(verticalPoints) || checkCombinations(horizontalPoints)
//				|| checkCombinations(diagonal1Points) || checkCombinations(diagonal2Points);
//
//		return isEnded;
//	}
//
//	private boolean checkCombinations(List<Point2D> points)
//	{
//		int chain = 0;
//		for (Point2D point: points)
//		{
//			int rowIndexForArray = (int) point.getX();
//			int columnIndexForArray = (int) point.getY();
//
//			Disc disc = getDiscIfPresent(rowIndexForArray,columnIndexForArray);
//			if (disc != null && disc.isPlayerOneMove == isPlayerOneTurn)
//			{
//				chain++;
//				if (chain == 4) return true;
//			}
//			else chain = 0;
//		}
//		return false;
//	}
//
//	private Disc getDiscIfPresent(int row, int column) {    // To prevent ArrayIndexOutOfBoundException
//
//		if (row >= ROWS || row < 0 || column >= COLUMNS || column < 0)  // If row or column index is invalid
//			return null;
//
//		return insertedDiscsArray[row][column];
//	}
//
//	private void gameOver() {
//		String winner = isPlayerOneTurn ? playerOne: playerTwo;
//		System.out.println("Winner is: " + winner);
//
//		Alert alert = new Alert(Alert.AlertType.INFORMATION);
//		alert.setTitle("Connect Four");
//		alert.setHeaderText("The Winner is " + winner);
//		alert.setContentText("Want to play again? ");
//
//		ButtonType yesBtn = new ButtonType("Yes");
//		ButtonType noBtn = new ButtonType("No, Exit");
//		alert.getButtonTypes().setAll(yesBtn, noBtn);
//
//		Platform.runLater(() -> { // Helps us to resolve IllegalStateException.
//
//			Optional<ButtonType> btnClicked = alert.showAndWait();
//			if (btnClicked.isPresent() && btnClicked.get() == yesBtn ) {
//				// ... user chose YES so RESET the game
//				resetGame();
//			} else {
//				// ... user chose NO .. so Exit the Game
//				Platform.exit();
//				System.exit(0);
//			}
//		});
//	}
//
//	public void resetGame() {
//
//		insertedDiscPane.getChildren().clear();    // Remove all Inserted Disc from Pane
//
//		for (int row = 0; row < insertedDiscsArray.length; row++) { // Structurally, Make all elements of insertedDiscsArray[][] to null
//			for (int col = 0; col < insertedDiscsArray[row].length; col++) {
//				insertedDiscsArray[row][col] = null;
//			}
//		}
//
//		isPlayerOneTurn = true; // Let player start the game
//		playerNameLabel.setText(playerOne);
//
//		createPlayground(); // Prepare a fresh playground
//	}

//	private static class Disc extends Circle {
//
//		private final boolean isPlayerOneMove;
//
//		public Disc(boolean isPlayerOneMove){
//			this.isPlayerOneMove = isPlayerOneMove;
//			setRadius(CIRCLE_DIAMETER/2);
//			setCenterX(CIRCLE_DIAMETER/2);
//			setCenterY(CIRCLE_DIAMETER/2);
//			setFill(isPlayerOneMove?Color.valueOf(DISC_COLOR1):Color.valueOf(DISC_COLOR2));
//		}
//	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
package de.unikl.seda.snake.gui.tools.cache.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.unikl.seda.snake.gui.tools.Icon.Icon;
import de.unikl.seda.snake.gui.tools.ObstacleType;
import de.unikl.seda.snake.gui.tools.backgroundType.Backgrounds;
import de.unikl.seda.snake.gui.tools.exception.SnakeException;
import de.unikl.seda.snake.gui.tools.foodType.FoodType;
import de.unikl.seda.snake.gui.tools.snakeparts.SnakeParts;

public class ImageCacheCreation implements ImageCache {
    private static final String IMAGE_FILE_PATTERN = "%s%s.png";

    private static final String ICONS_FOLDER = "/resource/img/icons/";

    private static final String BACKGROUND_FOLDER = "/resource/img/backgrounds/";

    private static final String SNAKE_FOLDER = "/resource/img/snake/";

    private static final String FOOD_FOLDER = "/resource/img/food/";

    private static final String MISC = "/resource/img/misc/";

    private final Map<ObstacleType, Image> obstacleImages;

    private final Map<SnakeParts, Image> snakePartImages;

    private final Map<FoodType, Image> foodTypes;

    private final Map<Icon, Image> iconTypes;

    private final Map<Backgrounds, Image> backgroundTypes;

    public ImageCacheCreation(){
        this.obstacleImages = createObstacleImages();
        this.snakePartImages = createSnakePartImages();
        this.foodTypes = createFoodTypeImages();
        this.iconTypes = createIconImages();
        this.backgroundTypes = createBackgroundImages();
    }

    @Override
    public Image getIcon(final Icon icon) {
        return iconTypes.get(icon);
    }

    @Override
    public Image getFoodImage(FoodType foodType) {
        return foodTypes.get(foodType);
    }

    @Override
    public Image getSnakePartImage(final SnakeParts snakePart) {
        return snakePartImages.get(snakePart);
    }

    @Override
    public  Image getObstacleImage(final ObstacleType obstacleType){
        return obstacleImages.get(obstacleType);
    }

    @Override
    public  Image getBackgroundImage(final Backgrounds backgroundType){
        return backgroundTypes.get(backgroundType);
    }

    private static Image createIcon(final Icon icon) {
        return createImage(ICONS_FOLDER, icon.name().toLowerCase());
    }

    private static Image createSnakePartImage(final SnakeParts snakePart) {
        return createImage(SNAKE_FOLDER, snakePart.name().toLowerCase());
    }

    private static Image createFoodType(final FoodType foodType){
        return createImage(FOOD_FOLDER, foodType.name().toLowerCase());
    }

    private static Image createObstacleImage(final ObstacleType obstacleType){
        return createImage(MISC, obstacleType.name().toLowerCase());
    }

    private static Image createBackGroundImage(final Backgrounds backgrounds){
        return createImage(BACKGROUND_FOLDER, backgrounds.name().toLowerCase());
    }

    private static Image createImage(final String imageFolder, final String fileName) {
        final String resourceName = String.format(IMAGE_FILE_PATTERN, imageFolder, fileName);
        try (InputStream inputStream = ImageCacheCreation.class.getResourceAsStream(resourceName)) {
            return  ImageIO.read(inputStream);
        } catch (final IOException e) {
            e.printStackTrace();
            throw new SnakeException(e);
        }
    }

    private static Map<Icon, Image> createIconImages() {
        return Arrays.stream(Icon.values()).collect(Collectors.toMap(Function.identity(),
                ImageCacheCreation::createIcon));
    }

    private static Map<SnakeParts, Image> createSnakePartImages() {
        return Arrays.stream(SnakeParts.values()).collect(Collectors.toMap(Function.identity(),
                ImageCacheCreation::createSnakePartImage));
    }

    private static Map<FoodType, Image> createFoodTypeImages(){
        return Arrays.stream(FoodType.values()).collect(Collectors.toMap(Function.identity(),
                ImageCacheCreation::createFoodType));
    }

    private static Map<ObstacleType, Image> createObstacleImages(){
        return Arrays.stream(ObstacleType.values()).collect(Collectors.toMap(Function.identity(),
                ImageCacheCreation::createObstacleImage));
    }

    private Map<Backgrounds, Image> createBackgroundImages() {
        return Arrays.stream(Backgrounds.values()).collect(Collectors.toMap(Function.identity(),
                ImageCacheCreation::createBackGroundImage));
    }
}

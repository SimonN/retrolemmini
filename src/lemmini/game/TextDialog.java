package lemmini.game;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import lemmini.graphics.GraphicsContext;
import lemmini.graphics.LemmImage;

/*
 * FILE MODIFIED BY RYAN SAKOWSKI
 *
 *
 * Copyright 2009 Volker Oth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Class to create text screens which can be navigated with the mouse.
 * Uses {@link LemmFont} as bitmap font.
 *
 * @author Volker Oth
 */
public class TextDialog {

    /** list of images */
    private final Map<String, List<TextDialogImage>> images;
    /** list of buttons */
    private final Map<String, List<Button>> buttons;

    private LemmImage backgroundImage;
    private boolean tileBackground;

    /**
     * Create dialog text screen.
     */
    public TextDialog() {
        buttons = new LinkedHashMap<>();
        images = new LinkedHashMap<>();
    }

    /**
     * Clear the text screen.
     */
    public void clear() {
        synchronized (images) {
            images.clear();
        }
        synchronized (buttons) {
            buttons.clear();
        }
        backgroundImage = null;
    }

    /**
     * Draw the text screen to the given graphics object.
     * @param g graphics object to draw the text screen to
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void drawScreen(GraphicsContext g, int x, int y, int width, int height) {
        int widthHalf = width / 2;
        int heightHalf = height / 2;
        int centerX = widthHalf + x;
        int centerY = heightHalf + y;

        if (backgroundImage != null) {
            if (tileBackground) {
                int imageWidth = backgroundImage.getWidth();
                int imageHeight = backgroundImage.getHeight();
                int xMin = (widthHalf % imageWidth == 0) ? 0 : ((centerX - (widthHalf / imageWidth + 1) * imageWidth));
                int yMin = (heightHalf % imageHeight == 0) ? 0 : ((centerY - (heightHalf / imageHeight + 1) * imageHeight));
                for (int xa = xMin; xa < x + width; xa += imageWidth) {
                    for (int ya = yMin; ya < y + width; ya += imageHeight) {
                        g.drawImage(backgroundImage, xa, ya);
                    }
                }
            } else {
                int xa = x + (width - backgroundImage.getWidth()) / 2;
                int ya = y + (height - backgroundImage.getHeight()) / 2;
                g.drawImage(backgroundImage, xa, ya);
            }
        }
        synchronized (images) {
            images.keySet().stream().forEachOrdered(s -> {
                images.get(s).stream().forEachOrdered(img -> {
                    img.draw(g, centerX, centerY);
                });
            });
        }
        synchronized (buttons) {
            buttons.keySet().stream().forEachOrdered(s -> {
                buttons.get(s).stream().forEachOrdered(b -> {
                    b.draw(g, centerX, centerY);
                });
            });
        }
    }

    /**
     * Set Image as background.
     * @param image Image to use as background
     * @param tiled If true, the image will be tiled. Otherwise, the image will
     *              be centered.
     */
    public void setBackground(final LemmImage image, final boolean tiled) {
        backgroundImage = image;
        tileBackground = true;
    }

    /**
     * Draw string.
     * @param s String
     * @param group Image group
     * @param x0 X position relative to center expressed in character widths
     * @param y0 Y position relative to center expressed in character heights
     * @param col LemmFont color
     */
    public void addString(final String s, final String group,
            final int x0, final int y0, final LemmFont.LemmColor col) {
        if (LemmFont.getCharCount(s) <= 0) {
            return;
        }
        int width = LemmFont.getWidth();
        int height = LemmFont.getHeight();
        List<String> sa = LemmFont.split(s, 0);
        synchronized (images) {
            addImageGroup(group);
            List<TextDialogImage> groupList = images.get(group);
            int x = x0 * width;
            for (ListIterator<String> lit = sa.listIterator(); lit.hasNext(); ) {
                int i = lit.nextIndex();
                String line = lit.next();
                if (LemmFont.getCharCount(line) > 0) {
                    int y = (y0 + i) * (height + 4);
                    groupList.add(new TextDialogImage(line, x, y, col));
                }
            }
        }
    }

    /**
     * Draw string.
     * @param s String
     * @param group Image group
     * @param x X position relative to center expressed in character widths
     * @param y Y position relative to center expressed in character heights
     */
    public void addString(final String s, final String group, final int x, final int y) {
        addString(s, group, x, y, LemmFont.LemmColor.GREEN);
    }

    /**
     * Draw string horizontally centered.
     * @param s String
     * @param group Image group
     * @param y0 Y position relative to center expressed in character heights
     * @param col LemmFont color
     */
    public void addStringCentered(final String s, final String group,
            final int y0, final LemmFont.LemmColor col) {
        if (LemmFont.getCharCount(s) <= 0) {
            return;
        }
        int width = LemmFont.getWidth();
        int height = LemmFont.getHeight();
        List<String> sa = LemmFont.split(s, Math.max(1, 800 / width));
        synchronized (images) {
            addImageGroup(group);
            List<TextDialogImage> groupList = images.get(group);
            for (ListIterator<String> lit = sa.listIterator(); lit.hasNext(); ) {
                int i = lit.nextIndex();
                String line = lit.next();
                int charCount = LemmFont.getCharCount(line);
                if (charCount > 0) {
                    if (charCount % 2 > 0) {
                        charCount = (charCount + 2) - charCount % 2;
                    }
                    int y = (y0 + i) * (height + 4);
                    int x = -(charCount * width / 2);
                    groupList.add(new TextDialogImage(line, x, y, col));
                }
            }
        }
    }

    /**
     * Draw string horizontally centered.
     * @param s String
     * @param group Image group
     * @param y Y position relative to center expressed in character heights
     */
    public void addStringCentered(final String s, final String group, final int y) {
        addStringCentered(s, group, y, LemmFont.LemmColor.GREEN);
    }

    /**
     * Add an image.
     * @param img Image
     * @param group Image group
     * @param x X position relative to center
     * @param y Y position relative to center
     */
    public void addImage(final LemmImage img, final String group, final int x, final int y) {
        synchronized (images) {
            addImageGroup(group);
            images.get(group).add(new TextDialogImage(img, x, y));
        }
    }

    /**
     * Add a horizontally centered image.
     * @param img Image
     * @param group Image group
     * @param y Y position relative to center
     */
    public void addImage(final LemmImage img, final String group, final int y) {
        int x = -(img.getWidth() / 2);
        synchronized (images) {
            addImageGroup(group);
            images.get(group).add(new TextDialogImage(img, x, y));
        }
    }

    /**
     * Add Button.
     * @param img Button image
     * @param imgSelected Button selected image
     * @param group Button group
     * @param x X position relative to center in pixels
     * @param y Y position relative to center in pixels
     * @param type Button type
     */
    public void addButton(final LemmImage img, final LemmImage imgSelected, LemmImage imgPressed, final String group,
            final int x, final int y, final TextScreen.Button type) {
        Button b = new Button(x, y, type);
        b.SetImage(img);
        b.SetImageSelected(imgSelected);
        b.SetImagePressed(imgPressed);
        synchronized (buttons) {
            addButtonGroup(group);
            buttons.get(group).add(b);
        }
    }

    /**
     * Add text button.
     * @param type Button type
     * @param t Button text
     * @param group Button group
     * @param x0 X position relative to center (in characters)
     * @param y0 Y position relative to center (in characters)
     * @param ts Button selected text
     * @param col Button text color
     * @param cols Button selected text color
     */
    public void addTextButton(final String t, final String ts, final String group,
            final int x0, final int y0, final TextScreen.Button type,
            final LemmFont.LemmColor textCol, final LemmFont.LemmColor selectedCol) {
        int x = x0 * LemmFont.getWidth();
        int y = y0 * (LemmFont.getHeight() + 4);
        TextButton b = new TextButton(x, y, type);
        b.setText(t, textCol);
        b.setTextSelected(ts, selectedCol);
        synchronized (buttons) {
            addButtonGroup(group);
            buttons.get(group).add(b);
        }
    }

    /**
     * React on left click.
     * @param x X position in pixels relative to center
     * @param y Y position in pixels relative to center
     * @return Button type if button clicked, else NONE
     */
    public TextScreen.Button handleLeftClick(final int x, final int y) {
        synchronized (buttons) {
            for (List<Button> bl : buttons.values()) {
                for (Button b : bl) {
                    if (b.inside(x, y)) {
                    	b.pressed = b.inside(x, y);
                        return b.type;
                    }
                }
            }
        }
        return TextScreen.Button.NONE;
    }
    
    public void handleMouseReleased() {
        synchronized (buttons) {
            for (List<Button> bl : buttons.values()) {
                for (Button b : bl) {
                    	b.pressed = false;
                }
            }
        }
    }

    /**
     * React on mouse hover.
     * @param x X position relative to center
     * @param y Y position relative to center
     */
    public void handleMouseMove(final int x, final int y) {
        synchronized (buttons) {
            buttons.values().stream().forEach(bl -> {
                bl.stream().forEach(b -> {
                    b.selected = b.inside(x, y);
                });
            });
        }
    }

    public void clearGroup(String group) {
        synchronized (images) {
            if (images.containsKey(group)) {
                images.get(group).clear();
            }
        }
        synchronized (buttons) {
            if (buttons.containsKey(group)) {
                buttons.get(group).clear();
            }
        }
    }

    private void addImageGroup(String group) {
        if (!images.containsKey(group)) {
            images.put(group, new ArrayList<>(16));
        }
    }

    private void addButtonGroup(String group) {
        if (!buttons.containsKey(group)) {
            buttons.put(group, new ArrayList<>(16));
        }
    }
}

/**
 * Button class for TextDialog.
 * @author Volker Oth
 */
class Button {
	/** x coordinate in pixels */
    private final int x;
    /** y coordinate in pixels */
    private final int y;
    /** width in pixels */
    protected int width;
    /** height in pixels */
    protected int height;
    /** button type */
    protected TextScreen.Button type;
    /** true if button is selected */
    protected boolean selected;
    /** true if button is pressed */
    public boolean pressed;
    /** normal button image */
    protected LemmImage image;
    /** selected button image */
    protected LemmImage imgSelected;
    /** pressed button image */
    protected LemmImage imgPressed;

    /**
     * Constructor
     * @param xi x position in pixels
     * @param yi y position in pixels
     * @param idi button type
     */
    Button(final int xi, final int yi, final TextScreen.Button typei) {
        x = xi;
        y = yi;
        type = typei;
        width = 0;
        height = 0;
        selected = false;
        pressed = false;
        image = null;
        imgSelected = null;
        imgSelected = null;
    }

    /**
     * Set normal button image.
     * @param img image
     */
    void SetImage(final LemmImage img) {
        image = img;
        if (image.getHeight() > height) {
            height = image.getHeight();
        }
        if (image.getWidth() > width) {
            width = image.getWidth();
        }
    }

    /**
     * Set selected button image.
     * @param img image
     */
    void SetImageSelected(final LemmImage img) {
        imgSelected = img;
        if (imgSelected.getHeight() > height) {
            height = imgSelected.getHeight();
        }
        if (imgSelected.getWidth() > width) {
            width = imgSelected.getWidth();
        }
    }
    
    /**
     * Set selected button image.
     * @param img image
     */
    void SetImagePressed(final LemmImage img) {
        imgPressed = img;
        if (imgPressed.getHeight() > height) {
            height = imgPressed.getHeight();
        }
        if (imgPressed.getWidth() > width) {
            width = imgPressed.getWidth();
        }
    }

    /**
     * Return current button image (normal or selected, depending on state).
     * @return current button image
     */
    LemmImage getImage() {
    	if (pressed) {
    		return imgPressed;
    	} else if (selected) {
            return imgSelected;
        } else {
            return image;
        }
    }

    /**
     * Draw the button.
     * @param g graphics object to draw on
     * @param cx
     * @param cy
     */
    void draw(final GraphicsContext g, final int cx, final int cy) {
        LemmImage img = getImage();
        if (img != null) {
            g.drawImage(getImage(), cx + x, cy + y);
        }
    }

    /**
     * Check if a (mouse) position is inside this button.
     * @param xi
     * @param yi
     * @return true if the coordinates are inside this button, false if not
     */
    boolean inside(final int xi, final int yi) {
        return (xi >= x && xi < x + width && yi >= y && yi < y + height);
    }
}

/**
 * Button class for TextDialog.
 * @author Volker Oth
 */
class TextButton extends Button {
    /**
     * Constructor
     * @param xi x position in pixels
     * @param yi y position in pixels
     * @param typei button type
     */
    TextButton(final int xi, final int yi, final TextScreen.Button typei) {
        super(xi, yi, typei);
    }

    /**
     * Set text which is used as button.
     * @param s String which contains the button text
     * @param color Color of the button (LemmFont color!)
     */
    void setText(final String s, final LemmFont.LemmColor color) {
        if (LemmFont.getCharCount(s) <= 0) {
            return;
        }
        image = LemmFont.strImage(s, color);
        if (image.getHeight() > height) {
            height = image.getHeight();
        }
        if (image.getWidth() > width) {
            width = image.getWidth();
        }
    }

    /**
     * Set text for selected button.
     * @param s String which contains the selected button text
     * @param color Color of the button (LemmFont color!)
     */
    void setTextSelected(final String s, final LemmFont.LemmColor color) {
        if (LemmFont.getCharCount(s) <= 0) {
            return;
        }
        imgSelected = LemmFont.strImage(s, color);
        if (imgSelected.getHeight() > height) {
            height = imgSelected.getHeight();
        }
        if (imgSelected.getWidth() > width) {
            width = imgSelected.getWidth();
        }
    }
}

class TextDialogImage {

    /** x coordinate in pixels */
    private final int x;
    /** y coordinate in pixels */
    private final int y;
    /** string image */
    protected LemmImage image;

    TextDialogImage(final LemmImage img, final int xi, final int yi) {
        x = xi;
        y = yi;
        image = img;
    }

    TextDialogImage(final String text, final int xi, final int yi, final LemmFont.LemmColor col) {
        x = xi;
        y = yi;
        image = LemmFont.strImage(text, col);
    }

    /**
     * Draw the image.
     * @param g graphics object to draw on
     * @param cx
     * @param cy
     */
    void draw(final GraphicsContext g, final int cx, final int cy) {
        g.drawImage(image, cx + x, cy + y);
    }
}
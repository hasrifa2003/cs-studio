package org.csstudio.saverestore.ui.browser;

import java.util.List;
import java.util.Optional;

import org.csstudio.saverestore.BaseLevel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.scene.Node;

/**
 *
 * <code>BaseLevelBrowser</code> provides the UI for the browsing mechanism of base levels. This can be a tree,
 * table, list, or any other component, as long is provides means to retrieve the selected base level and a few
 * other features described below.
 *
 * @author <a href="mailto:jaka.bobnar@cosylab.com">Jaka Bobnar</a>
 *
 * @param <T>
 */
public interface BaseLevelBrowser<T extends BaseLevel> {

    /**
     * Returns the title to be used for the container of this browser based on the provided arguments. The title
     * may contain the two given parameters or it may also ignore them. Null return values are not accepted, but
     * empty strings are.
     *
     * @param baseLevel the selected base level
     * @param branch the selected branch
     * @return a title composed of parameters, but not necessary
     */
    public String getTitleFor(Optional<T> baseLevel, Optional<String> branch);

    /**
     * @return the JavaFX node, which contains all the UI nodes used by this browser
     */
    public Node getFXContent();

    /**
     * Toggles whether only those base levels for which the beamline sets already exist or all base levels
     * should be available.
     *
     * @param onlyAvailable true if only existing are available or false if all are available in the table
     */
    public void setShowOnlyAvailable(boolean onlyAvailable);

    /**
     * Returns the property that contains the list of all available base levels. This type of base levels
     * is not defined for this property in order to allow setting any type on the browser. The browser should take
     * care of transforming the type to the type that it understands and can work with.
     *
     * @return the property that contains the list of all available base levels
     */
    public ObjectProperty<List<T>> availableBaseLevelsProperty();

    public List<T> transform(List<? extends BaseLevel> list);
    /**
     * @return the property that provides the selected base level
     */
    public Property<T> baseLevelProperty();
}

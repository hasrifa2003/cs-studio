/*
 * Copyright (c) 2010 Stiftung Deutsches Elektronen-Synchrotron, Member of the Helmholtz
 * Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS. WITHOUT WARRANTY OF ANY
 * KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE IN ANY RESPECT, THE USER ASSUMES
 * THE COST OF ANY NECESSARY SERVICING, REPAIR OR CORRECTION. THIS DISCLAIMER OF WARRANTY
 * CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE. NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER
 * EXCEPT UNDER THIS DISCLAIMER. DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
 * ENHANCEMENTS, OR MODIFICATIONS. THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION,
 * MODIFICATION, USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY AT
 * HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 */
package org.csstudio.sds.behavior.desy;

import org.csstudio.sds.components.model.SimpleSliderModel;
import org.csstudio.sds.cursorservice.CursorService;
import org.csstudio.sds.model.AbstractWidgetModel;
import org.epics.css.dal.simple.AnyData;
import org.epics.css.dal.simple.MetaData;

/**
 *
 * Default DESY-Behavior for the {@link SimpleSliderModel} widget with Connection state.
 *
 * @author hrickens
 * @author $Author: hrickens $
 * @version $Revision: 1.4 $
 * @since 20.04.2010
 */
public class SimpleSliderConnectionBehavior extends AbstractDesyConnectionBehavior<SimpleSliderModel> {

    /**
     * Constructor.
     */
    public SimpleSliderConnectionBehavior() {
        addInvisiblePropertyId(SimpleSliderModel.PROP_VALUE);
        addInvisiblePropertyId(SimpleSliderModel.PROP_MAX);
        addInvisiblePropertyId(SimpleSliderModel.PROP_MIN);
    }

    @Override
    protected void doProcessValueChange(final SimpleSliderModel model, final AnyData anyData) {
        super.doProcessValueChange(model, anyData);
        // .. update slider value
        model.setPropertyValue(SimpleSliderModel.PROP_VALUE, anyData.doubleValue());
    }

    @Override
    protected void doProcessMetaDataChange(final SimpleSliderModel widget, final MetaData meta) {
        if (meta != null) {
            // .. update min / max
            widget.setPropertyValue(SimpleSliderModel.PROP_MAX, meta.getDisplayHigh());
            widget.setPropertyValue(SimpleSliderModel.PROP_MIN, meta.getDisplayLow());
            switch (meta.getAccessType()) {
                case NONE:
                    widget.setPropertyValue(AbstractWidgetModel.PROP_CURSOR, CursorService.getInstance()
                            .availableCursors().get(7));
                    break;
                case READ:
                    widget.setPropertyValue(AbstractWidgetModel.PROP_CURSOR, CursorService.getInstance()
                            .availableCursors().get(7));
                    break;
                case READ_WRITE:
                    widget.setPropertyValue(AbstractWidgetModel.PROP_CURSOR, CursorService.getInstance()
                            .availableCursors().get(0));
                    break;
                case WRITE:
                    widget.setPropertyValue(AbstractWidgetModel.PROP_CURSOR, CursorService.getInstance()
                            .availableCursors().get(0));
                    break;
                default:
                    widget.setPropertyValue(AbstractWidgetModel.PROP_CURSOR, CursorService.getInstance()
                            .availableCursors().get(0));
            }
        }
    }

    @Override
    protected String[] doGetSettablePropertyIds() {
        return new String[] { SimpleSliderModel.PROP_VALUE };
    }
}

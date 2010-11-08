package org.csstudio.opibuilder.widgets.editparts;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;

/**The locator of label cell editor.
 * @author Xihui Chen
 *
 */
public class LabelCellEditorLocator
		implements CellEditorLocator
	{

		private Figure labelFigure;
	
		public LabelCellEditorLocator(Figure stickyNote) {
			setLabel(stickyNote);
		}
	
		public void relocate(CellEditor celleditor) {
			Text text = (Text)celleditor.getControl();
			Rectangle rect = labelFigure.getClientArea();
			labelFigure.translateToAbsolute(rect);
			org.eclipse.swt.graphics.Rectangle trim = text.computeTrim(0, 0, 0, 0);
			rect.translate(trim.x, trim.y);
			rect.width += trim.width;
			rect.height += trim.height;
			text.setBounds(rect.x, rect.y, rect.width, rect.height);
		}
	
		/**
		 * Returns the stickyNote figure.
		 */
		protected Figure getLabel() {
			return labelFigure;
		}
	
		/**
		 * Sets the Sticky note figure.
		 * @param stickyNote The stickyNote to set
		 */
		protected void setLabel(Figure stickyNote) {
			this.labelFigure = stickyNote;
		}


	}
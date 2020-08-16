package dlr.ses.core;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import dlr.ses.seseditor.SESEditor;

/**
 * <h1>EditorUndoableEditListener</h1>
 * <p>
 * This class implements the undo and redo options of the JTree. 
 * </p>
 * 
 * @author Bikash Chandra Karmokar
 * @version 1.0
 *
 */
public class EditorUndoableEditListener implements UndoableEditListener {
	public void undoableEditHappened(UndoableEditEvent e) {
		// Remember the edit and update the menus
		SESEditor.undoJtree.addEdit(e.getEdit());
	}
}

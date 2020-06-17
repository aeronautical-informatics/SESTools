package dlr.ses.core;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import dlr.ses.peseditor.PESEditor;

public class EditorUndoableEditListener implements UndoableEditListener {
	public void undoableEditHappened(UndoableEditEvent e) {
		// Remember the edit and update the menus
		PESEditor.undoJtree.addEdit(e.getEdit());
		// undoAction.updateUndoState();
		// redoAction.updateRedoState();
	}
}

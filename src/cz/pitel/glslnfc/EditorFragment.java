package cz.pitel.glslnfc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditorFragment extends Fragment {
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		/*
		final View view = inflater.inflate(R.layout.editor, container, false);
		final EditText editor = (EditText) view.findViewById(R.id.editor);
		editor.setHorizontallyScrolling(true);
		//editor.setText(getActivity().getPreferences(Context.MODE_PRIVATE).getString("shader", "blah"));
		editor.setText("java");
		*/
		return inflater.inflate(R.layout.editor, container, false);
	}

	@Override
	public void onResume() {
		((EditText) getActivity().findViewById(R.id.editor)).setText("java");
		((EditText) getView().findViewById(R.id.editor)).setText("java");
	}
}

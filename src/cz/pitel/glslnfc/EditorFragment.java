package cz.pitel.glslnfc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditorFragment extends Fragment {
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.editor, container, false);
		final EditText editor = (EditText) view.findViewById(R.id.editor);
		editor.setHorizontallyScrolling(true);	//Does not work from XML
		return view;
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setText(getActivity().getPreferences(Context.MODE_PRIVATE).getString("shader", getString(R.string.default_shader)));
	}

	public void setText(final CharSequence text) {
		((EditText) getView().findViewById(R.id.editor)).setText(text);
	}
}

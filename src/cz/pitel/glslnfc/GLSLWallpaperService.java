package cz.pitel.glslnfc;

import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class GLSLWallpaperService extends WallpaperService {
	@Override
	public Engine onCreateEngine() {
		return new GLSLWallpaperEngine();
	}

	private class GLSLWallpaperEngine extends Engine {
		private final GLSLRenderer renderer = new GLSLRenderer();

		@Override
		public void onSurfaceCreated(final SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
		}
	}
}

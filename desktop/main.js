const { app, BrowserWindow, shell, dialog } = require('electron');
const path = require('path');

function createWindow() {
  const win = new BrowserWindow({
    width: 1180,
    height: 820,
    minWidth: 360,
    minHeight: 600,
    autoHideMenuBar: true,
    backgroundColor: '#f3f7f7',
    webPreferences: {
      nodeIntegration: false,
      contextIsolation: true,
      sandbox: true
    }
  });

  // Packaged builds contain the web app inside desktop/web.
  // During local development, load the repository-level web folder.
  const indexPath = app.isPackaged
    ? path.join(__dirname, 'web', 'index.html')
    : path.join(__dirname, '..', 'web', 'index.html');

  win.loadFile(indexPath).catch((error) => {
    console.error('Unable to load Amar Hisab:', error);
    dialog.showErrorBox(
      'Amar Hisab could not start',
      `The application files could not be found.\n\nExpected: ${indexPath}\n\n${error.message}`
    );
  });

  win.webContents.setWindowOpenHandler(({ url }) => {
    if (url.startsWith('https://') || url.startsWith('http://')) shell.openExternal(url);
    return { action: 'deny' };
  });

  win.webContents.on('will-navigate', (event, url) => {
    if (!url.startsWith('file://')) {
      event.preventDefault();
      if (url.startsWith('https://') || url.startsWith('http://')) shell.openExternal(url);
    }
  });
}

app.whenReady().then(() => {
  app.setAppUserModelId('com.amarhisab.desktop');
  createWindow();
  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) createWindow();
  });
});

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') app.quit();
});
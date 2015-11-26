package com.faforever.client.chat;

import com.faforever.client.audio.AudioController;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.notification.TransientNotification;
import com.faforever.client.player.PlayerService;
import com.faforever.client.preferences.ChatPrefs;
import com.faforever.client.preferences.Preferences;
import com.faforever.client.preferences.PreferencesService;
import com.faforever.client.test.AbstractPlainJavaFxTest;
import javafx.scene.control.TabPane;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.time.Instant;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class PrivateChatTabControllerTest extends AbstractPlainJavaFxTest {

  @Mock
  PreferencesService preferencesService;
  @Mock
  Preferences preferences;
  @Mock
  ChatPrefs chatPrefs;
  @Mock
  PlayerService playerService;
  @Mock
  AudioController audioController;
  @Mock
  NotificationService notificationService;

  private PrivateChatTabController instance;
  private String playerName;

  @Before
  public void setUp() throws IOException {
    instance = loadController("private_chat_tab.fxml");
    instance.preferencesService = preferencesService;
    instance.playerService = playerService;
    instance.audioController = audioController;
    instance.notificationService = notificationService;

    playerName = "testUser";
    PlayerInfoBean playerInfoBean = new PlayerInfoBean(playerName);

    when(preferencesService.getPreferences()).thenReturn(preferences);
    when(preferences.getChat()).thenReturn(chatPrefs);
    when(playerService.getPlayerForUsername(playerName)).thenReturn(playerInfoBean);

    TabPane tabPane = new TabPane();
    WaitForAsyncUtils.waitForAsyncFx(5000, () -> getRoot().getChildren().add(tabPane));
    tabPane.getTabs().add(instance.getRoot());
  }

  @Test
  public void testOnChatMessageUnfocusedTriggersNotification() throws Exception {
    WaitForAsyncUtils.waitForAsyncFx(5000, () -> getRoot().getScene().getWindow().hide());
    instance.onChatMessage(new ChatMessage(Instant.now(), playerName, "Test message"));
    verify(notificationService).addNotification(any(TransientNotification.class));
  }

  @Test
  public void testOnChatMessageFocusedDoesntTriggersNotification() throws Exception {
    instance.onChatMessage(new ChatMessage(Instant.now(), playerName, "Test message"));
    verifyZeroInteractions(notificationService);
  }

  @Test
  public void onChatMessageTestNotFoeShowFoe() {
    when(chatPrefs.getHideFoeMessages()).thenReturn(false);
    instance.onChatMessage(new ChatMessage(Instant.now(), playerName, "Test message"));
  }

  @Ignore("Not yet implemented")
  @Test
  public void onChatMessageTestNotFoeHideFoe() {

  }

  @Ignore("Not yet implemented")
  @Test
  public void onChatMessageTestIsFoeShowFoe() {

  }

  @Ignore("Not yet implemented")
  @Test
  public void onChatMessageTestIsFoeHideFoe() {

  }
}

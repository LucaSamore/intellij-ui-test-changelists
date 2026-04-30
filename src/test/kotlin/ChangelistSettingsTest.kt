import com.intellij.driver.sdk.ui.components.common.IdeaFrameUI
import com.intellij.driver.sdk.ui.components.common.ideFrame
import com.intellij.driver.sdk.ui.components.elements.button
import com.intellij.driver.sdk.ui.components.elements.checkBoxWithName
import com.intellij.driver.sdk.ui.components.settings.SettingsDialogUiComponent
import com.intellij.driver.sdk.ui.components.settings.clickOkBtnAndCloseDialog
import com.intellij.driver.sdk.ui.components.settings.settingsDialog
import com.intellij.ide.starter.community.model.BuildType
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import com.intellij.ide.starter.ide.IDETestContext
import com.intellij.ide.starter.ide.IdeProductProvider
import com.intellij.ide.starter.junit5.hyphenateWithClass
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.project.GitHubProject
import com.intellij.ide.starter.runner.CurrentTestMethod
import com.intellij.ide.starter.runner.Starter
import kotlin.time.Duration.Companion.minutes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ChangelistSettingsTest {

    companion object {
        private const val SETTINGS_GROUP = "Version Control"
        private const val SETTINGS_SECTION = "Changelists"
        private const val CHECKBOX_NAME = "Create changelists automatically"
    }

    @Test
    fun `enable create changelists automatically`() = runTest {
        withChangelistSettings {
            content {
                val checkbox = checkBoxWithName(CHECKBOX_NAME)
                if (!checkbox.isSelected()) {
                    checkbox.click()
                }
                assertTrue(checkbox.isSelected()) { "Checkbox should be selected" }
            }
            clickOkBtnAndCloseDialog()
        }
    }

    @Test
    fun `setting is persisted after clicking OK and reopening settings`() = runTest {
        withChangelistSettings {
            content {
                val checkbox = checkBoxWithName(CHECKBOX_NAME)
                if (!checkbox.isSelected()) {
                    checkbox.click()
                }
            }
            clickOkBtnAndCloseDialog()
        }

        withChangelistSettings {
            content {
                val checkbox = checkBoxWithName(CHECKBOX_NAME)
                assertTrue(checkbox.isSelected()) {
                    "Checkbox should still be selected after closing and reopening settings"
                }
            }
        }
    }

    @Test
    fun `setting is not persisted after reopening settings without clicking OK`() = runTest {
        var initialCheckboxState = false

        withChangelistSettings {
            content {
                val checkbox = checkBoxWithName(CHECKBOX_NAME)
                initialCheckboxState = checkbox.isSelected()
                checkbox.click()
            }
            button("Cancel").click()
        }

        withChangelistSettings {
            content {
                val checkbox = checkBoxWithName(CHECKBOX_NAME)
                assertEquals(initialCheckboxState, checkbox.isSelected()) {
                    "Checkbox state should not have changed after clicking Cancel"
                }
            }
        }
    }

    private fun runTest(testBody: IdeaFrameUI.() -> Unit) {
        configureTestContext().runIdeWithDriver().useDriverAndCloseIde {
            ideFrame {
                waitForIndicators(5.minutes)
                testBody()
            }
        }
    }

    private fun IdeaFrameUI.withChangelistSettings(action: SettingsDialogUiComponent.() -> Unit) {
        openSettingsDialog()
        settingsDialog {
            openTreeSettingsSection(SETTINGS_GROUP, SETTINGS_SECTION)
            action()
        }
    }

    private fun configureTestContext(): IDETestContext =
        Starter.newContext(
                CurrentTestMethod.hyphenateWithClass(),
                TestCase(
                    ideInfo = IdeProductProvider.IC.copy(buildType = BuildType.RELEASE.type),
                    projectInfo =
                        GitHubProject.fromGithub(
                            branchName = "master",
                            repoRelativeUrl = "Perfecto-Quantum/Quantum-Starter-Kit.git",
                            commitHash = "1dc6128c115cb41fc442c088174e81f63406fad5",
                        ),
                ),
            )
            .prepareProjectCleanImport()
}

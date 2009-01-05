/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.ivyde.eclipse.ui.preferences;

import org.apache.ivy.Ivy;
import org.apache.ivyde.eclipse.IvyPlugin;
import org.apache.ivyde.eclipse.cpcontainer.IvyClasspathInitializer;
import org.apache.ivyde.eclipse.ui.AcceptedSuffixesTypesComposite;
import org.apache.ivyde.eclipse.ui.RetrieveComposite;
import org.apache.ivyde.eclipse.ui.SettingsEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osgi.framework.Constants;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By
 * subclassing <samp>FieldEditorPreferencePage</samp>, we can use the field support built into JFace
 * that allows us to create a page that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that
 * belongs to the main plug-in class. That way, preferences can be accessed directly via the
 * preference store.
 */

public class IvyPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    /** the ID of the preference page */
    public static final String PEREFERENCE_PAGE_ID = "org.apache.ivyde.eclipse.ui.preferences.IvyPreferencePage";

    private RetrieveComposite retrieveComposite;

    private SettingsEditor settingsEditor;

    private Button resolveInWorkspaceCheck;

    private Combo alphaOrderCheck;

    private AcceptedSuffixesTypesComposite acceptedSuffixesTypesComposite;

    private Text organizationText;

    private Text organizationUrlText;

    private Button refreshOnStartupButton;

    private Button resolveOnStartupButton;

    private Button doNothingButton;

    public IvyPreferencePage() {
        setPreferenceStore(IvyPlugin.getDefault().getPreferenceStore());
        Object ivydeVersion = IvyPlugin.getDefault().getBundle().getHeaders().get(
            Constants.BUNDLE_VERSION);
        setDescription("Ivy " + Ivy.getIvyVersion() + " (" + Ivy.getIvyDate() + ")  --  IvyDE "
                + ivydeVersion);
    }

    public void init(IWorkbench workbench) {
        setPreferenceStore(IvyPlugin.getDefault().getPreferenceStore());
    }

    protected Control createContents(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        composite.setLayout(new GridLayout());

        // CheckStyle:MagicNumber| OFF

        Label horizontalLine = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        horizontalLine.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

        Group startupGroup = new Group(composite, SWT.NONE);
        startupGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        startupGroup.setLayout(new GridLayout());
        startupGroup.setText("On Eclipse startup");

        doNothingButton = new Button(startupGroup, SWT.RADIO);
        doNothingButton.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        doNothingButton.setText("Do nothing");

        refreshOnStartupButton = new Button(startupGroup, SWT.RADIO);
        refreshOnStartupButton
                .setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        refreshOnStartupButton.setText("Trigger refresh");

        resolveOnStartupButton = new Button(startupGroup, SWT.RADIO);
        resolveOnStartupButton
                .setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        resolveOnStartupButton.setText("Trigger resolve");

        Group settingsGroup = new Group(composite, SWT.NONE);
        settingsGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        settingsGroup.setLayout(new GridLayout());
        settingsGroup.setText("Global settings");

        settingsEditor = new SettingsEditor(settingsGroup, SWT.NONE);
        settingsEditor.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

        Group retrieveGroup = new Group(composite, SWT.NONE);
        retrieveGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        retrieveGroup.setLayout(new GridLayout());
        retrieveGroup.setText("Retrieve configuration");

        retrieveComposite = new RetrieveComposite(retrieveGroup, SWT.NONE);
        retrieveComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

        Group containerGroup = new Group(composite, SWT.NONE);
        containerGroup.setText("Classpath container configuration");
        containerGroup.setLayout(new GridLayout(3, false));
        containerGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

        resolveInWorkspaceCheck = new Button(containerGroup, SWT.CHECK);
        resolveInWorkspaceCheck.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true,
                false, 3, 1));
        resolveInWorkspaceCheck.setText("Resolve dependencies in workspace (EXPERIMENTAL)");
        resolveInWorkspaceCheck
                .setToolTipText("Will replace jars on the classpath with workspace projects");

        Label label = new Label(containerGroup, SWT.NONE);
        label.setText("Order of the classpath entries:");

        alphaOrderCheck = new Combo(containerGroup, SWT.READ_ONLY);
        alphaOrderCheck
                .setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1));
        alphaOrderCheck.setToolTipText("Order of the artifacts in the classpath container");
        alphaOrderCheck.add("From the ivy.xml");
        alphaOrderCheck.add("Lexical");

        acceptedSuffixesTypesComposite = new AcceptedSuffixesTypesComposite(containerGroup,
                SWT.NONE);
        acceptedSuffixesTypesComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
                true, false, 3, 1));

        Group editorGroup = new Group(composite, SWT.NONE);
        editorGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 3, 1));
        editorGroup.setLayout(new GridLayout(2, false));
        editorGroup.setText("Editor information");

        label = new Label(editorGroup, SWT.NONE);
        label.setText("Organisation:");
        organizationText = new Text(editorGroup, SWT.SINGLE | SWT.BORDER);
        organizationText
                .setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));

        label = new Label(editorGroup, SWT.NONE);
        label.setText("Organisation URL:");
        organizationUrlText = new Text(editorGroup, SWT.SINGLE | SWT.BORDER);
        organizationUrlText.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true,
                false));
        // CheckStyle:MagicNumber| ON

        initPreferences();

        return composite;
    }

    private void initPreferences() {
        IvyDEPreferenceStoreHelper helper = IvyPlugin.getPreferenceStoreHelper();
        switch (helper.getResolveOnStartup()) {
            case IvyClasspathInitializer.ON_STARTUP_NOTHING:
                doNothingButton.setSelection(true);
                break;
            case IvyClasspathInitializer.ON_STARTUP_REFRESH:
                refreshOnStartupButton.setSelection(true);
                break;
            case IvyClasspathInitializer.ON_STARTUP_RESOLVE:
                resolveOnStartupButton.setSelection(true);
                break;
        }
        settingsEditor.init(helper.getIvySettingsPath(), helper.getPropertyFiles(), helper
                .getLoadSettingsOnDemand());
        retrieveComposite.init(helper.getDoRetrieve(), helper.getRetrievePattern(), helper
                .getRetrieveConfs(), helper.getRetrieveTypes(), helper.getRetrieveSync());
        resolveInWorkspaceCheck.setSelection(helper.isResolveInWorkspace());
        alphaOrderCheck.select(helper.isAlphOrder() ? 1 : 0);
        acceptedSuffixesTypesComposite.init(helper.getAcceptedTypes(), helper.getSourceTypes(),
            helper.getSourceSuffixes(), helper.getJavadocTypes(), helper.getJavadocSuffixes());
        organizationText.setText(helper.getIvyOrg());
        organizationUrlText.setText(helper.getIvyOrgUrl());
    }

    public boolean performOk() {
        IvyDEPreferenceStoreHelper helper = IvyPlugin.getPreferenceStoreHelper();
        helper.setIvySettingsPath(settingsEditor.getSettingsPath());
        if (doNothingButton.getSelection()) {
            helper.setResolveOnStartup(IvyClasspathInitializer.ON_STARTUP_NOTHING);
        } else if (refreshOnStartupButton.getSelection()) {
            helper.setResolveOnStartup(IvyClasspathInitializer.ON_STARTUP_REFRESH);
        } else {
            helper.setResolveOnStartup(IvyClasspathInitializer.ON_STARTUP_RESOLVE);
        }
        helper.setPropertyFiles(settingsEditor.getPropertyFiles());
        helper.setDoRetrieve(retrieveComposite.isRetrieveEnabled());
        helper.setRetrievePattern(retrieveComposite.getRetrievePattern());
        helper.setRetrieveSync(retrieveComposite.isSyncEnabled());
        helper.setRetrieveConfs(retrieveComposite.getRetrieveConfs());
        helper.setRetrieveTypes(retrieveComposite.getRetrieveTypes());
        helper.setResolveInWorkspace(resolveInWorkspaceCheck.getSelection());
        helper.setAlphOrder(alphaOrderCheck.getSelectionIndex() == 1);
        helper.setAcceptedTypes(acceptedSuffixesTypesComposite.getAcceptedTypes());
        helper.setSourceTypes(acceptedSuffixesTypesComposite.getSourcesTypes());
        helper.setSourceSuffixes(acceptedSuffixesTypesComposite.getSourceSuffixes());
        helper.setJavadocTypes(acceptedSuffixesTypesComposite.getJavadocTypes());
        helper.setJavadocSuffixes(acceptedSuffixesTypesComposite.getJavadocSuffixes());
        helper.setOrganization(organizationText.getText());
        helper.setOrganizationUrl(organizationUrlText.getText());
        return true;
    }

    protected void performDefaults() {
        switch (PreferenceInitializer.DEFAULT_RESOLVE_ON_STARTUP) {
            case IvyClasspathInitializer.ON_STARTUP_NOTHING:
                doNothingButton.setSelection(true);
                break;
            case IvyClasspathInitializer.ON_STARTUP_REFRESH:
                refreshOnStartupButton.setSelection(true);
                break;
            case IvyClasspathInitializer.ON_STARTUP_RESOLVE:
                resolveOnStartupButton.setSelection(true);
                break;
        }
        settingsEditor.init(PreferenceInitializer.DEFAULT_IVYSETTINGS_PATH,
            PreferenceInitializer.DEFAULT_PROPERTY_FILES,
            PreferenceInitializer.DEFAULT_LOAD_SETTINGS_ON_DEMAND);
        retrieveComposite.init(PreferenceInitializer.DEFAULT_DO_RETRIEVE,
            PreferenceInitializer.DEFAULT_RETRIEVE_PATTERN,
            PreferenceInitializer.DEFAULT_RETRIEVE_CONFS,
            PreferenceInitializer.DEFAULT_RETRIEVE_TYPES,
            PreferenceInitializer.DEFAULT_RETRIEVE_SYNC);
        resolveInWorkspaceCheck.setSelection(PreferenceInitializer.DEFAULT_RESOLVE_IN_WORKSPACE);
        alphaOrderCheck.select(PreferenceInitializer.DEFAULT_ALPHABETICAL_ORDER ? 1 : 0);
        acceptedSuffixesTypesComposite.init(PreferenceInitializer.DEFAULT_ACCEPTED_TYPES,
            PreferenceInitializer.DEFAULT_SOURCES_TYPES,
            PreferenceInitializer.DEFAULT_SOURCES_SUFFIXES,
            PreferenceInitializer.DEFAULT_JAVADOC_TYPES,
            PreferenceInitializer.DEFAULT_JAVADOC_SUFFIXES);
        organizationText.setText(PreferenceInitializer.DEFAULT_ORGANISATION);
        organizationUrlText.setText(PreferenceInitializer.DEFAULT_ORGANISATION_URL);
    }
}

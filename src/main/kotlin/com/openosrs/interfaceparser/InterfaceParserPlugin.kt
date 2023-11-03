/*
 * Copyright (c) 2023, Melxin <https://github.com/melxin>
 * All rights reserved.
 *
 * This code is licensed under GPL3, see the complete license in
 * the LICENSE file in the root directory of this source tree.
 */
package com.openosrs.interfaceparser

import org.gradle.api.Plugin
import org.gradle.api.Project

class InterfaceParserPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            tasks.create("createInterfaceComponents", InterfaceParse::class.java)
        }
    }
}
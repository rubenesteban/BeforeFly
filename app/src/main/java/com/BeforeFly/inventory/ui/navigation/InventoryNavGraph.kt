/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.BeforeFly.inventory.ui.navigation


import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import beforefly.R
import com.BeforeFly.inventory.Home
import com.BeforeFly.inventory.HomenDestination
import com.BeforeFly.inventory.ui.home.HomeDestination
import com.BeforeFly.inventory.ui.home.HomeScreen
import com.BeforeFly.inventory.ui.item.*
import com.example.Work
import com.example.WorkDestination



/**
 * Provides Navigation graph for the application.
 */
@Composable
fun InventoryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomenDestination.route,
        modifier = modifier
    ) {
        composable(route = HomenDestination.route) {
            val context = LocalContext.current
            Home(
                navigateToScreen = { navController.navigate(HomeDestination.route) },
                //navigateToPrio = { navController.navigate(WorkDestination.route) },
                onSendButtonClicked = { subject: String, summary: String ->
                    shareOrder(context = context, subject = subject, summary = summary)
                },

                )
        }
        composable(route = WorkDestination.route) {
            val context = LocalContext.current
            Work(
               // navigateToScreen = { navController.navigate(HomeDestination.route) },
                //navigateToPrio = { navController.navigate(HomenDestination.route) },
                onSendButtonClicked = { subject: String, summary: String ->
                    shareOrder(context = context, subject = subject, summary = summary)
                },

                )
        }
        composable(route = HomeDestination.route) {
            HomeScreen(
                onNavigateUp = { navController.navigateUp() },
                navigateToItemEntry = { navController.navigate(ItemEntryDestination.route) },
                navigateToItemUpdate = {
                    navController.navigate("${ItemDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = ItemEntryDestination.route) {
            ItemEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemDetailsScreen(
                navigateToEditItem = { navController.navigate("${ItemEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

    }
}




fun shareOrder(context: Context, subject: String, summary: String) {
    // Create an ACTION_SEND implicit intent with order details in the intent extras
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.new_cupcake_order)
        )
    )
}
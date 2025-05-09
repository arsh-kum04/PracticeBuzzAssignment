package com.example.assignment

import android.R.attr.clickable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignment.ui.theme.AssignmentTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssignmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NotesScreen(
                        innerPadding = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun NotesScreen(
    notesViewModel: NotesViewModel = viewModel(),
    innerPadding: Modifier,
) {
    val notesList by notesViewModel.listOfNotes.collectAsState()
    val isFormVisible by notesViewModel.isFormVisible.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .then(innerPadding),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    notesViewModel.toggleFormVisibility()
                }
            ) {
                val icon =
                    if (isFormVisible) Icons.Rounded.CheckCircle else Icons.Rounded.ArrowForward
                val contentDescription = if (isFormVisible) "Hide Form" else "Show Form"
                Icon(icon, contentDescription = contentDescription)
            }
        },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.End
    ) { scaffoldInnerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState(), enabled = true)
                .imePadding()
                .padding(24.dp)
                .padding(scaffoldInnerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isFormVisible) {
                NotesForm(notesViewModel = notesViewModel)
            }
            NotesList(notesList = notesList)
        }
    }
}

@Composable
fun NotesForm(notesViewModel: NotesViewModel) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = title,
                onValueChange = { title = it },
                label = "Title",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                isError = title.isEmpty(),
                leadingIcon = {
                    Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = if (title.isEmpty()) Color.Gray else Color(0xFF1E88E5)
                    )
                }
            )

            CustomTextField(
                value = desc,
                onValueChange = { desc = it },
                label = "Description",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                isError = desc.isEmpty(),
                leadingIcon = {
                    Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = if (desc.isEmpty()) Color.Gray else Color(0xFF1E88E5)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            val isFormValid = desc.isNotEmpty() && title.isNotEmpty()

            Button(
                onClick = { notesViewModel.addNote(UiState(title, desc)) },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E88E5),
                    disabledContainerColor = Color(0xFFE0E0E0)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Proceed",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isFormValid) Color.White else Color.Gray
                    )
                    if (isFormValid) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotesList(notesList: List<UiState>) {
    LazyColumn(
        modifier = Modifier.height(400.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(notesList) { note ->
            NoteItem(note = note)
        }
    }
}

@Composable
fun NoteItem(note: UiState) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

                Toast.makeText(context, note.content, Toast.LENGTH_SHORT).show()
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        shape = RoundedCornerShape(8.dp),

        ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = note.title, fontWeight = FontWeight.Bold)
            Text(text = note.content)
        }
    }
}


@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF1E88E5),
            unfocusedBorderColor = Color.Gray,
            errorBorderColor = Color.Red,
            focusedLabelColor = Color(0xFF1E88E5),
            unfocusedLabelColor = Color.Gray
        ),
        keyboardOptions = keyboardOptions,
        leadingIcon = leadingIcon,
        textStyle = MaterialTheme.typography.bodyMedium.copy(Color.Black)
    )
}
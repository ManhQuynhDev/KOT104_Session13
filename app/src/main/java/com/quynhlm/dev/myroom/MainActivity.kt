package com.quynhlm.dev.myroom

import android.app.Application
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.quynhlm.dev.myroom.Models.Note
import com.quynhlm.dev.myroom.ViewModel.NoteViewModel
import com.quynhlm.dev.myroom.ui.theme.MyRoomTheme

class MainActivity : ComponentActivity() {
    private val noteViewModel: NoteViewModel by lazy {
        ViewModelProvider(this, NoteViewModel.NoteViewModelFactory(this.application)).get(
            NoteViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyRoomTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NoteScreen(noteViewModel = noteViewModel, application = application)
                }
            }
        }
    }
}

@Composable
fun NoteScreen(noteViewModel: NoteViewModel, application: Application?) {
    val context = LocalContext.current
    val noteList = remember { mutableStateOf<List<Note>>(emptyList()) }
    noteViewModel.getAllNote().observe(LocalLifecycleOwner.current, Observer { notes ->
        noteList.value = notes
    })

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .fillMaxHeight(0.4f)
                .background(Color.White),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "TodoList", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                placeholder = { Text(text = "Title") }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                value = content,
                onValueChange = { content = it },
                placeholder = { Text(text = "Content") },
                maxLines = 3
            )
            Button(
                onClick = {
                    val note = Note(title, content)
                    noteViewModel.insertNote(note)
                    Toast.makeText(context, "Note added successfully", Toast.LENGTH_SHORT).show()
                    title = ""
                    content = ""
                }
            ) {
                Text(text = "Save")
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .fillMaxHeight(0.6f)
        ) {
            Text(text = "List of Notes")
            LazyColumn {
                items(noteList.value) { note ->
                    NoteItem(noteViewModel = noteViewModel, note = note)
                }
            }
        }
    }
}

@Composable
fun NoteItem(noteViewModel: NoteViewModel, note: Note) {
    var showDialog by remember { mutableStateOf(false) }
    var showDialogUpdate by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Color.LightGray.copy(alpha = 0.5f))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = note.title, fontWeight = FontWeight.Bold)
                Text(text = note.content)
            }
            Row {
                Text(modifier = Modifier.clickable {
                    showDialog = true
                }, text = "Xóa", color = Color.Red)
                if (showDialog == true) {
                    CustomDialog(onDismiss = { showDialog = false },
                        onDelete = {
                            try {
                                noteViewModel.deleteNote(note)
                                Toast.makeText(context,"Xóa thành công" , Toast.LENGTH_SHORT).show()
                                showDialog = false
                            }catch (e : Exception){
                                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
                Text(text = " ")
                Text(modifier = Modifier.clickable {
                    showDialogUpdate= true
                }, text = "Sửa", color = Color.Blue)

                if (showDialogUpdate == true) {
                    CustomDialogUpdate(note = note,onDismiss = { showDialogUpdate = false },noteViewModel = noteViewModel!!)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialogUpdate(note: Note, onDismiss: () -> Unit, noteViewModel: NoteViewModel) {
    var titleUpdate by remember { mutableStateOf(note.title) }
    var contentUpdate by remember { mutableStateOf(note.content) }
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier.size(width = 350.dp, height = 300.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Sửa sản phẩm")
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = titleUpdate,
                    onValueChange = { titleUpdate = it },
                    placeholder = { Text(text = "Title") }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    value = contentUpdate,
                    onValueChange = { contentUpdate = it },
                    placeholder = { Text(text = "Content") },
                    maxLines = 3
                )
                Button(onClick = {
                    try {
                        val updatedNote = Note(
                            id = note.id,
                            title = titleUpdate,
                            content = contentUpdate
                        )
                        noteViewModel.update(updatedNote)
                        Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Sửa thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text(text = "Update")
                }
            }
        }
    }
}
@Composable
fun CustomDialog(onDismiss: () -> Unit, onDelete: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Xóa note", fontSize = 20.sp)
        },
        text = {
            Text("Bạn có chắc chắn muốn xóa note này không ?.")
        },
        confirmButton = {
            Button(onClick = onDelete) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun NoteScreenPreview() {
    MyRoomTheme {
        NoteScreen(noteViewModel = NoteViewModel(Application()), application = Application())
    }
}
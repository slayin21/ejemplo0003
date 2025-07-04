let equipoAModificar;

function verEquipos() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
         if (this.readyState == 4 && this.status == 200) {
             const equipos = JSON.parse(this.responseText);
             rellenarDiv(equipos, "listado");
         } else {
            rellenarError(this, "listado");
         }
    };
    xhttp.open("GET", "http://localhost:8088/demo/equipos", true);
    xhttp.send();
}

function verEquipo() {
    let idEquipo = document.getElementById("aConsultar").value;
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
         if (this.readyState == 4 && this.status == 200) {
             const equipo = JSON.parse(this.responseText);
             equipoAModificar = equipo;
             rellenarDiv(equipo, "consulta");
             prepararModificacion(equipo);
         } else {
            rellenarError(this, "consulta");
         }
    };
    xhttp.open("GET", "http://localhost:8088/demo/equipos/" + idEquipo, true);
    xhttp.send();
}

function crearEquipo() {
    let idEquipo = document.getElementById("idACrear").value;
    let nombre = document.getElementById("nombreACrear").value;
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
         if (this.readyState == 4 && this.status == 200) {
             const equipo = JSON.parse(this.responseText);
             rellenarDiv(equipo, "creado");
         } else {
            rellenarError(this, "creado");
         }
    };
    xhttp.open("POST", "http://localhost:8088/demo/equipos?id=" + idEquipo + "&nombre=" + nombre, true);
    xhttp.send();
}

function modificarEquipo() {
    let idEquipo = document.getElementById("idAModificar").value;
    let nombre = document.getElementById("nombreAModificar").value;
    let equipo = equipoAModificar;
    equipo.nombre = nombre;
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
         if (this.readyState == 4 && this.status == 200) {
             const equipoJSON = JSON.parse(this.responseText);
             rellenarDiv(equipoJSON, "modificado");
         } else {
            rellenarError(this, "modificado");
         }
    };
    xhttp.open("PUT", "http://localhost:8088/demo/equipos/" + idEquipo, true);
    xhttp.setRequestHeader("Content-type", "application/json; charset=utf-8");
    xhttp.send(JSON.stringify(equipo));
}

function anadirJugador() {
    let idEquipo = document.getElementById("idEquipoAnadir").value;
    let idJugador = document.getElementById("idJugadorAnadir").value;
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
         if (this.readyState == 4 && this.status == 200) {
             const equipo = JSON.parse(this.responseText);
             rellenarDiv(equipo, "anadido");
         } else {
            rellenarError(this, "anadido");
         }
    };
    xhttp.open("PUT", "http://localhost:8088/demo/equipos/" + idEquipo + "/" + idJugador, true);
    xhttp.send();
}

function borrarEquipo() {
    let idEquipo = document.getElementById("aBorrar").value;
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
         if (this.readyState == 4 && this.status == 200) {
             const equipo = JSON.parse(this.responseText);
             rellenarDiv(equipo, "borrado");
         } else {
            rellenarError(this, "borrado");
         }
    };
    xhttp.open("DELETE", "http://localhost:8088/demo/equipos/" + idEquipo, true);
    xhttp.send();
}

function prepararModificacion(equipo) {
    document.getElementById("idAModificar").value = equipo.id;
    document.getElementById("nombreAModificar").value = equipo.nombre;
}

function rellenarError(datosError, idDiv) {
    const divError = document.getElementById("error");
    divError.innerHTML = "Status: " + datosError.status;
    const div = document.getElementById(idDiv);
    div.innerHTML = "";
}

function rellenarDiv(datos, idDiv) {
    const div = document.getElementById(idDiv);
    div.innerHTML = "";
    if (datos.length) {
        div.appendChild(mostrarLista(datos));        
    } else {
        div.appendChild(mostrarElemento(datos));
    }
}

function mostrarElemento(elemento) {
    let span = document.createElement("span");
    let todo = "";
    if (typeof elemento == "string") {
        todo = elemento;
    } else {
        return mostrarLista(elemento);
    }   
    span.innerHTML = todo;
    return span;
}

function mostrarLista(elementos) {
    let table = document.createElement("table");
    let cabeceras = [];
    let esLista;
    for (let key in elementos) {
        if (isNaN(parseInt(key))) {
            cabeceras.push(key);
            esLista = false;
        } else {
            esLista = true;
            for (let key2 in elementos[key]) {
                if (cabeceras.indexOf(key2) === -1) {
                    cabeceras.push(key2);
                }
            }
        }
    }     
    let trCab = table.insertRow(-1);
    for (let i = 0; i < cabeceras.length; i++) {
        let th = document.createElement("th");
        th.innerHTML = cabeceras[i];
        trCab.appendChild(th);
    }
    if (esLista) {
        for (let key in elementos) {
            let tr = table.insertRow(-1);
            for (let j = 0; j < cabeceras.length; j++) {
                var casilla = tr.insertCell(-1);
                casilla.appendChild(mostrarElemento(elementos[key][cabeceras[j]], false));
            }
        }
    } else {
        let tr = table.insertRow(-1);
        for (let key in elementos) {
            var casilla = tr.insertCell(-1);
            casilla.appendChild(mostrarElemento(elementos[key], false));
        }
    }
    return table;
}
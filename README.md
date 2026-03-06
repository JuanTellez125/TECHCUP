# TECHCUP

## Nombre del equipo
**Testigos de Jehóva**

## Nombre de los integrantes
**- Cristian Adrian Ducuara Quiñonez**

**- Cristian Ronaldo Guerrero Buitrago**

**- Javier Mauricio Deaquiz Romero**

**- Juan Esteban Tellez Valencia**

**- Juan Sebastian González Aranguren**

## Titulo del proyecto
**TECHCUP**

## Enunciado del problema
Los programas de Ingeniería de Sistemas, IA, Ciberseguridad y Estadística realizan cada semestre un torneo interno de 
fútbol en el que participan estudiantes de distintos semestres. Aunque la actividad tiene alta acogida, su organización 
actualmente depende de procesos manuales: mensajes por WhatsApp, formularios aislados y hojas de cálculo.
Esta forma de manejo genera desorden, retrasos y confusión entre participantes y organizadores. Muchos estudiantes no 
conocen el proceso de inscripción, los equipos se completan tarde, los pagos se verifi can manualmente y la información 
del torneo (fechas, reglas, resultados) se encuentra dispersa.
TECHCUP FÚTBOL propone desarrollar una plataforma web que centralice toda la gestión del torneo, permitiendo que 
estudiantes, capitanes y organizadores interactúen en un solo sistema organizado y transparente.

Actualmente, el torneo presenta las siguientes dificultades:

● El proceso de inscripción no es claro para los participantes.

● Los capitanes tienen problemas para completar sus equipos.

● Los pagos no se verifican de forma rápida y es un proceso manual.

● Los resultados y la tabla de posiciones se actualizan manualmente.

● Las llaves eliminatorias se organizan a mano.

● No existe historial ni estadísticas del torneo.

● La información oficial está dispersa.

● Esto ocasiona retrasos, errores administrativos y conflictos entre los participantes.

## Índice

### **- Diagrama de contexto:**

![Captura](/docs/uml/DiagramaContextoTECHCUP.png)

### **- Definicion de requerimientos:**

**- Funcionales:**
**- RF01 – Gestión de Torneos** El organizador puede crear un torneo con fecha inicial, fecha final, cantidad de equipos y 
costo por equipo. El torneo tendrá estados: Borrador, Activo, En progreso y Finalizado. El organizador puede iniciar, 
finalizar y consultar torneos.

**- RF02 – Configuración del Torneo** El organizador puede definir reglamento, fechas importantes, cierre de inscripciones, 
horarios de partidos, canchas disponibles y sanciones aplicables.

**- RF03 – Registro de Usuarios** Los estudiantes y graduados se registran con correo institucional; los familiares con correo
Gmail. Cada usuario puede crear un perfil deportivo con posición de juego, número dorsal y foto.

**- RF04 – Disponibilidad y búsqueda de jugadores** Un jugador puede marcarse como disponible para ser contactado. Los 
capitanes pueden buscar jugadores por posición, semestre, edad, género, nombre e identificación.

**- RF05 – Gestión de Equipos** Un capitán puede crear un equipo, asignar nombre, subir escudo y definir colores del uniforme.
Puede invitar jugadores y estos pueden aceptar o rechazar la invitación.

**- RF06 – Validación de reglas de equipo** El sistema valida que el equipo tenga entre 7 y 12 jugadores, ningún jugador 
pertenezca a dos equipos simultáneamente, más de la mitad de los miembros pertenezcan a los programas elegibles y no se 
permitan cambios de jugadores una vez iniciado el torneo.

**- RF07 – Inscripción y pago** El capitán sube el comprobante de pago (Nequi o efectivo). El organizador revisa el 
comprobante y cambia el estado del equipo: Pendiente a En revisión a Aprobado o Rechazado. Solo los equipos aprobados 
participan en el torneo.

**- RF08 – Gestión de alineaciones** El capitán puede seleccionar titulares y reservas, elegir formación táctica y ubicar 
jugadores visualmente en el campo antes de cada partido. Capitanes y jugadores pueden consultar la alineación del equipo
rival.

**- RF09 – Registro de resultados** El organizador registra marcador, goleadores, tarjetas amarillas y tarjetas rojas de cada 
partido.

**- RF10 – Consulta de partidos por árbitro** El árbitro puede consultar la fecha, hora, cancha y equipos del partido que le 
corresponde arbitrar.

**- RF11 – Tabla de posiciones** El sistema calcula automáticamente por equipo: partidos jugados, ganados, empatados y 
perdidos, goles a favor, goles en contra, diferencia de gol y puntos totales.

**- RF12 – Llaves eliminatorias** El sistema genera automáticamente los cruces iniciales de forma aleatoria y avanza las 
fases de cuartos de final, semifinal y final conforme se registran resultados.

**- RF13 – Estadísticas del torneo** El sistema permite consultar estadísticas generales del torneo activo, incluyendo el 
listado de máximos goleadores, cantidad de goles por jugador y ranking ordenado de mayor a menor. La información se 
actualiza automáticamente cada vez que se registran resultados de partidos.

**- RF14 – Historial de torneos** El sistema conserva la información de torneos anteriores y permite consultar por cada 
torneo: equipos participantes, tabla de posiciones final, resultados de partidos, campeones y estadísticas individuales.
La información histórica no podrá ser modificada una vez el torneo esté finalizado.

**- RF15 – Asignación de árbitros** El organizador puede asignar un árbitro a cada partido programado del torneo, definiendo 
qué árbitro será responsable de dirigir el encuentro. El sistema debe evitar que un mismo árbitro sea asignado a dos 
partidos en el mismo horario.

**- RF16 – Gestión de roles** El administrador del sistema puede asignar, modificar o revocar roles de usuario (Capitán, 
Organizador, Árbitro), garantizando que cada usuario tenga los permisos correspondientes a su rol dentro del sistema. 
Un usuario podrá tener más de un rol si así lo define el administrador.

**- RF17 – Consulta pública de información** El sistema permite a usuarios no autenticados consultar información general del
torneo, incluyendo reglamento, calendario de partidos, tabla de posiciones, resultados y llaves eliminatorias, 
garantizando transparencia en el desarrollo de la competencia.

**- RF18 – Gestión de sanciones automáticas** El sistema aplica automáticamente sanciones deportivas según el reglamento 
definido por el organizador. En caso de tarjeta roja, el jugador quedará suspendido para el siguiente partido del 
torneo, y el sistema impedirá su inclusión en la alineación correspondiente.

**- RF19 – Notificación de invitaciones** El sistema notifica a los jugadores cuando reciben una invitación para unirse a un
equipo. La notificación debe indicar el nombre del equipo y del capitán que envía la invitación, permitiendo al jugador 
aceptarla o rechazarla.

**- RF20 – Notificación de estado de inscripción** El sistema notifica al capitán cuando el organizador cambie el estado del
equipo (Pendiente, En revisión, Aprobado o Rechazado), indicando el resultado de la validación del comprobante de pago.

**- RF21 – Notificación de asignación de partido** El sistema notifica al árbitro cuando se le asigne un partido, indicando 
fecha, hora, cancha y equipos participantes, permitiendo su consulta dentro del módulo correspondiente.

**- No funcionales:**

**- RNF01 – Seguridad y autenticación** El sistema debe autenticar a los usuarios según su tipo: correo institucional para 
estudiantes, graduados y personal de la Escuela; correo Gmail para familiares. Debe implementarse control de roles y 
permisos por tipo de actor.
**- RNF02 – Auditoría** El sistema debe registrar un log de las acciones relevantes realizadas por los usuarios (creación de 
equipos, aprobación de pagos, registro de resultados, etc.) para trazabilidad.
**- RNF03 – Arquitectura por capas** El backend debe desarrollarse en Spring Boot con separación clara en capas: 
controladores, adaptadores, lógica de negocio y acceso a datos. La comunicación con el frontend se realizará mediante 
una API REST.
**- RNF05 – Seguridad de almacenamiento de contraseñas** Las contraseñas deben almacenarse utilizando un algoritmo de hashing
seguro (por ejemplo, BCrypt), garantizando que no se almacenen en texto plano.
**- RNF06 – Usabilidad** La plataforma debe ser lo suficientemente intuitiva para que estudiantes sin experiencia técnica 
avanzada puedan inscribirse, unirse a equipos y consultar información del torneo sin asistencia externa.
**- RNF07 – Disponibilidad** La plataforma debe estar disponible durante todo el período del torneo, especialmente en fechas 
clave como cierre de inscripciones y registro de resultados.
**- RNF08 – Escalabilidad básica** El sistema debe soportar la carga de todos los equipos participantes en un torneo semestral
sin degradación notable del rendimiento.
**- RNF09 – Compatibilidad** La aplicación debe ser compatible con las versiones actuales de los navegadores Google Chrome, 
Microsoft Edge y Mozilla Firefox.
**- RNF10 – Respaldo de información** La base de datos debe contar con respaldo automático diario para garantizar la 
recuperación de información ante fallos del sistema.
**- RNF11 – Escalabilidad básica** La arquitectura del sistema debe permitir aumentar la capacidad de usuarios concurrentes 
mediante escalamiento vertical del servidor sin necesidad de rediseño estructural.

## - Análisis de requerimientos

[Documento Word](docs/requirements/Analisis de Requerimientos.docx)

## Mockup

## Manual de identidad

## Link de Jira
package com.jvc.factunet.utilitarios;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.sql.Timestamp;

public class Fecha {

    public static final String FORMATO_DATE_F0 = "dd/MM/yyyy";
    public static final String FORMATO_DATE_F1 = "ddMMyyyy";
    public static final String FORMATO_DATE_F2 = "yyyy-MM-dd";
    public static final String FORMATO_DATE_TIME_F0 = "dd/MM/yyyy HH:mm:ss";
    public static final String FORMATO_DATE_TIME_F1 = "yyyy-MM-dd HH:mm:ss.S";
    public static final String FORMATO_DATE_TIME_F = "yyyy-MM-dd";
    public static final String FORMATO_TIME_F0 = "HH:mm";
    private static final String[] meses = {"Enero", "Febrero", "Marzo", "Abril",
        "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre",
        "Diciembre"};

    /**
     * Devuelve la fecha seteada a 00 horas 00 minutos 00 segundos en formato
     * calendar
     *
     * @param fecha fecha para transformar
     * @return fecha con formato de hora 00:00:00
     * @author cggm
     */
    public static Calendar fechaInicioCalendar(Date fecha) {
        Calendar fInicio = Calendar.getInstance();
        fInicio.setTime(fecha);
        fInicio.set(Calendar.HOUR_OF_DAY, 0);
        fInicio.set(Calendar.MINUTE, 0);
        fInicio.set(Calendar.SECOND, 0);
        fInicio.set(Calendar.MILLISECOND, 0);
        return fInicio;
    }

    /**
     * Devuelve la fecha seteada a 23 horas 59 minutos 59 segundos en formato
     * calendar
     *
     * @param fecha fecha para transformar
     * @return fecha con formato de hora 23:59:59
     * @author cggm
     */
    public static Calendar fechaFinCalendar(Date fecha) {
        Calendar fFin = Calendar.getInstance();
        fFin.setTime(fecha);
        fFin.set(Calendar.HOUR_OF_DAY, 23);
        fFin.set(Calendar.MINUTE, 59);
        fFin.set(Calendar.SECOND, 59);
        fFin.set(Calendar.MILLISECOND, 0);
        return fFin;
    }

    /**
     * Devuelve la fecha seteada a 00 horas 00 minutos 00 segundos
     *
     * @param fecha fecha para transformar
     * @return fecha con formato de hora 00:00:00
     * @author cggm
     */
    public static Date fechaInicio(Date fecha) {
        try {
            return fechaInicioCalendar(fecha).getTime();
        } catch (Exception e) {
            return fecha;
        }
    }

    /**
     * Devuelve la fecha seteada a 23 horas 59 minutos 59 segundos
     *
     * @param fecha fecha para transformar
     * @return fecha con formato de hora 23:59:59
     * @author cggm
     */
    public static Date fechaFin(Date fecha) {
        try {
            return fechaFinCalendar(fecha).getTime();
        } catch (Exception e) {
            return fecha;
        }
    }

    /**
     * Devuelve la fecha seteada a 00 horas 00 minutos 00 segundos a String
     *
     * @param fecha
     * @return Fecha seteada a 00 horas 00 minutos 00 segundos
     * @author cggm
     */
    public static String setFechaInicialString(Date fecha) {
        return formatoDateTimeToStringF0(fechaInicio(fecha));

    }

    /**
     * Devuelve la fecha seteada a 23 horas 59 minutos 59 segundos a String
     *
     * @param fecha
     * @return Fecha seteada a 23 horas 59 minutos 59 segundos
     * @author cggm
     */
    public static String setFechaFinalString(Date fecha) {
        return formatoDateTimeToStringF0(fechaFin(fecha));
    }

    /**
     * Convierte a String a partir de un Date con el formato dd/MM/yyyy HH:mm:ss
     *
     * @param fecha
     * @return String
     */
    public static String formatoDateTimeToStringF0(Date fecha) {
        SimpleDateFormat fechaFormato = new SimpleDateFormat(FORMATO_DATE_TIME_F0);
        return fechaFormato.format(fecha);
    }

    /**
     * Convierte a String a partir de un Date con el formato dd/MM/yyyy
     *
     * @param fecha
     * @return String
     */
    public static String formatoDateStringF0(Date fecha) {
        SimpleDateFormat fechaFormato = new SimpleDateFormat(FORMATO_DATE_F0);
        return fechaFormato.format(fecha);
    }

    /**
     * Convierte a String a partir de un Date con el formato ddMMyyyy
     *
     * @param fecha
     * @return String
     */
    public static String formatoDateStringF1(Date fecha) {
        SimpleDateFormat fechaFormato = new SimpleDateFormat(FORMATO_DATE_F1);
        return fechaFormato.format(fecha);
    }

    /**
     * Convierte a DateTime a partir de un String con el formato dd/MM/yyyy
     * HH:mm:ss
     *
     * @param fecha
     * @return Date
     * @throws ParseException
     */
    public static Date formatoStringToDateTimeF0(String fecha) throws ParseException {
        SimpleDateFormat fechaFormato = new SimpleDateFormat(FORMATO_DATE_TIME_F0);
        return fechaFormato.parse(fecha);
    }

    /**
     * Convierte a Date a partir de un string con el formato yyyy-MM-dd
     * HH:mm:ss:S. Se utiliza para la coversion de la fecha cuando es convertido
     * desde un objeto de un array.
     *
     * @param fecha
     * @return Date
     * @throws ParseException
     */
    public static Date formatoStringToDateTimeF2(String fecha) throws ParseException {
        SimpleDateFormat fechaFormato = new SimpleDateFormat(FORMATO_DATE_TIME_F1);
        return fechaFormato.parse(fecha);
    }

    /**
     * Convierte a Date a partir de un string con el formato yyyy/MM/dd
     *
     * @param fecha
     * @return Date
     * @throws ParseException
     */
    public static Date formatoStringToDateF0(String fecha) throws ParseException {
        SimpleDateFormat fechaFormato = new SimpleDateFormat(FORMATO_DATE_F0);
        return fechaFormato.parse(fecha);
    }

    /**
     * Convierte a Date a partir de un string con el formato ddMMyyyy
     *
     * @param fecha
     * @return Date
     * @throws ParseException
     */
    public static Date formatoStringToDateF1(String fecha) throws ParseException {
        SimpleDateFormat fechaFormato = new SimpleDateFormat(FORMATO_DATE_F1);
        return fechaFormato.parse(fecha);
    }

    public static Date formatoStringToDateF(String fecha) throws ParseException {
        SimpleDateFormat fechaFormato = new SimpleDateFormat(FORMATO_DATE_TIME_F);
        return fechaFormato.parse(fecha);
    }

    /**
     * Convierte a DateTime a partir de un string con el formato HH:mm
     *
     * @param fecha
     * @return Date
     * @throws ParseException
     */
    public static Date formatoStringToTime(String time) throws ParseException {
        SimpleDateFormat fechaFormato = new SimpleDateFormat(FORMATO_TIME_F0);
        return fechaFormato.parse(time);
    }

    /**
     * Forma una Date a partir de año, mes y dia
     *
     * @param year
     * @param mes
     * @param dia
     * @return Date
     * @author cggm
     */
    public static Date formarFecha(int year, int mes, int dia) {
        Calendar calendario = new GregorianCalendar(year, mes - 1, dia);
        return calendario.getTime();
    }

    /**
     * Forma una Date a partir de año, mes , dia , horas , minutos, segundos y
     * milisegundos
     *
     * @param year
     * @param mes
     * @param dia
     * @param hora
     * @param minutos
     * @return Fecha formada
     * @author cggm
     */
    public static Date formarFechaTiempo(int year, int mes, int dia, int hora, int minutos) {
        Calendar calendario = new GregorianCalendar(year, mes - 1, dia, hora, minutos);
        return calendario.getTime();
    }

    /**
     * @param fechaInicial
     * @param fechaFinal
     * @return numero de dias de diferencia entre 2 fechas
     * @author roberto
     */
    public static int diferenciaEntre2Fechas(Date fechaInicial, Date fechaFinal) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String fechaInicioString = df.format(fechaInicial);

        try {
            fechaInicial = df.parse(fechaInicioString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String fechaFinalString = df.format(fechaFinal);

        try {
            fechaFinal = df.parse(fechaFinalString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long fechaInicialMs = fechaInicial.getTime();
        long fechaFinalMs = fechaFinal.getTime();
        long diferencia = fechaFinalMs - fechaInicialMs;
        double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
        return ((int) dias);
    }

    /**
     * Establece a una fecha el parametro de milisegundos a 0
     *
     * @param fecha
     * @return Fecha con milisegundos seteado a 0.
     */
    public static Date fechaSinMilisegundos(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        calendario.set(Calendar.MILLISECOND, 0);
        return calendario.getTime();
    }

    /**
     * Permite sumar segundos a una fecha
     *
     * @author ALBERTO
     * @param fecha
     * @param cantidad
     * @return
     */
    public static Date sumarSegundo(Date fecha, int cantidad) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.SECOND, cantidad);
        return calendar.getTime();
    }

    /**
     * Permite sumar minutos a una fecha
     *
     * @author ALBERTO
     * @param fecha
     * @param cantidad
     * @return
     */
    public static Date sumarMinuto(Date fecha, int cantidad) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.MINUTE, cantidad);
        return calendar.getTime();
    }

    /**
     * Permite sumar horas a una fecha
     *
     * @author ALBERTO
     * @param fecha
     * @param cantidad
     * @return
     */
    public static Date sumarHora(Date fecha, int cantidad) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.HOUR, cantidad);
        return calendar.getTime();
    }

    /**
     * Permite sumar dias a una fecha
     *
     * @author ALBERTO
     * @param fecha
     * @param cantidad
     * @return
     */
    public static Date sumarDia(Date fecha, int cantidad) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_MONTH, cantidad);
        return calendar.getTime();
    }

    /**
     * Permite sumar meses a una fecha
     *
     * @param fecha
     * @param cantidad
     * @return
     */
    public static Date sumarMes(Date fecha, int cantidad) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.MONTH, cantidad);
        return calendar.getTime();
    }

    /**
     * Permite sumar años a una fecha
     *
     * @author ALBERTO
     * @param fecha
     * @param cantidad
     * @return
     */
    public static Date sumarAnios(Date fecha, int cantidad) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.YEAR, cantidad);
        return calendar.getTime();
    }

    /**
     * Método que setea el día de una fecha
     *
     * @param fecha fecha para transformar.
     * @param dia Día que se va a agregar a la fecha.
     * @return fecha con formato de hora 00:00:00
     * @author cggm
     */
    public static Date setDia(Date fecha, int dia) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        calendario.set(Calendar.DAY_OF_MONTH, dia);
        return calendario.getTime();
    }

    /**
     * Devuelve la fecha inicial de un mes y anio especificado Util para rango
     * de fechas conociendo el mes
     *
     * @author ALBERTO
     * @param mes
     * @param anio
     * @return
     *
     */
    public static Date setMesInicioAnio(int mes, int anio) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, mes - 1);
        c.set(Calendar.YEAR, anio);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return fechaInicio(c.getTime());
    }

    /**
     * Devuelve la fecha inicial de un mes de una fecha especifica.
     *
     * @param fecha
     * @return Fecha formateada con la fecha enviada con el dia de inicio de
     * mes.
     *
     */
    public static Date setMesInicio(Date fecha) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return fechaInicio(c.getTime());
    }

    /**
     * Devuelve la fecha final de un mes y anio especificado Util para rango de
     * fechas conociendo el mes
     *
     * @author ALBERTO
     * @param mes
     * @param anio
     * @return
     *
     */
    public static Date setMesFinAnio(int mes, int anio) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, mes - 1);
        c.set(Calendar.YEAR, anio);
        int dia = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, dia);
        return fechaFin(c.getTime());

    }

    /**
     * Devuelve la fecha final de un mes de una fecha especifica.
     *
     * @param fecha para formatear
     * @return Fecha formateada con la fecha enviada con el dia de fin de mes.
     *
     */
    public static Date setMesFin(Date fecha) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        int dia = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, dia);
        return fechaFin(c.getTime());

    }

    /**
     * obtengo el dia de una fecha.
     *
     * @param fecha Fecha para obtener el día
     * @return Día obtenido de la fecha
     *
     */
    public static int getDia(Date fecha) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * obtengo el mes de una fecha.
     *
     * @param fecha Fecha para obtener el mes
     * @return Mes obtenido de la fecha
     *
     */
    public static int getMes(Date fecha) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        int mes = c.get(Calendar.MONTH) + 1;
        return mes;
    }

    /**
     * obtengo el años de una fecha.
     *
     * @param fecha Fecha para obtener el año
     * @return Año obtenido de la fecha
     *
     */
    public static int getAnio(Date fecha) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        return c.get(Calendar.YEAR);
    }

    /**
     * Establece a la fecha1 las horas, minutos y segundos de la fecha2.
     *
     * @param fecha1 Fecha que mantiene el día, mes y año
     * @param fecha2 Fecha que se obtinen las horas minutos y segundos
     * @return Retorna una nueva fecha conformada por la fecha1 (Dia, mes, año)
     * mas la fecha2 (Horas,minutos, segundos)
     */
    public static Date formatearTiempo(Date fecha1, Date fecha2) {
        // Fecha 2 de la cual se obtiene las horas, minutos, segundos
        Calendar f2 = Calendar.getInstance();
        f2.setTime(fecha2);
        int hora = f2.get(Calendar.HOUR_OF_DAY);
        int minutos = f2.get(Calendar.MINUTE);
        int segundos = f2.get(Calendar.SECOND);

        // Fecha1 en la cual se establece la hora minutos, segundos de la fecha2
        Calendar f1 = Calendar.getInstance();
        f1.setTime(fecha1);
        f1.set(Calendar.HOUR_OF_DAY, hora);
        f1.set(Calendar.MINUTE, minutos);
        f1.set(Calendar.SECOND, segundos);
        f1.set(Calendar.MILLISECOND, 0);

        return f1.getTime();

    }

    /**
     * Establece a la fecha1 el día, mes y año de la fecha2.
     *
     * @param fecha1 Fecha que mantiene el la hora, minutos y segundos.
     * @param fecha2 Fecha que se el día, mes y año.
     * @return Retorna una nueva fecha conformada por la fecha1 (Hora, minutos,
     * segundos) mas la fecha2 (Día,mes, año)
     */
    public static Date formatearFecha(Date fecha1, Date fecha2) {
        // Fecha 2 de la cual se obtiene el día, mes y año
        Calendar f2 = Calendar.getInstance();
        f2.setTime(fecha2);
        int dia = f2.get(Calendar.DAY_OF_MONTH);
        int mes = f2.get(Calendar.MONTH);
        int anio = f2.get(Calendar.YEAR);

        // Fecha1 en la cual se establece el día, mes y año de la fecha2
        Calendar f1 = Calendar.getInstance();
        f1.setTime(fecha1);
        f1.set(Calendar.DAY_OF_MONTH, dia);
        f1.set(Calendar.MONTH, mes);
        f1.set(Calendar.YEAR, anio);
        f1.set(Calendar.MILLISECOND, 0);

        return f1.getTime();

    }

    /**
     * Permite obtener las horas entre dos fechas.
     *
     * @param horaInicio Fecha de inicio.
     * @param horaFin Fecha Fin.
     * @return
     */
    public static double calculoDeHoras(Date horaInicio, Date horaFin) {
        Calendar horaIni = Calendar.getInstance();
        horaIni.setTime(horaInicio);
        Calendar horaFn = Calendar.getInstance();
        horaFn.setTime(horaFin);
        double horas = horaFn.get(Calendar.HOUR_OF_DAY) - horaIni.get(Calendar.HOUR_OF_DAY);
        double mins = horaFn.get(Calendar.MINUTE) - horaIni.get(Calendar.MINUTE);
        mins /= 60;
        horas += mins;
        return horas;
    }

    public static long calcularDias(Date inicio, Date fin) {
        // Crear 2 instancias de Calendar
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        // Establecer las fechas
        cal1.setTime(inicio);
        cal2.setTime(fin);

        // conseguir la representacion de la fecha en milisegundos
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();

        // calcular la diferencia en milisengundos
        long diff = milis2 - milis1;


        // calcular la diferencia en dias
        long diffDays = diff / (24 * 60 * 60 * 1000);

        System.out.println("En dias: " + diffDays + " dias.");
        return diffDays;
    }

    /**
     * Obtiene el nombre del mes de una determinada fecha.
     *
     * @param fecha
     * @return
     */
    public static String nombreMes(final Date fecha) {
        int indice = fecha.getMonth();
        return meses[indice];
    }
    
    public static Timestamp utilDateToSQLTimestamp(Date date) 
    {
        try 
        {
            return new Timestamp(date.getTime());
        } 
        catch (Exception e) 
        { 
            return null;
        } 
    }
}

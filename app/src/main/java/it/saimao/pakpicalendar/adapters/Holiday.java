package it.saimao.pakpicalendar.adapters;

import java.time.LocalDate;

import it.saimao.pakpicalendar.mmcalendar.MyanmarDate;

public record Holiday (String holiday, LocalDate engDate, MyanmarDate myanmarDate) {}

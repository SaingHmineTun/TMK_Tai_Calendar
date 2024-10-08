package it.saimao.tmktaicalendar.adapters;

import java.time.LocalDate;

import it.saimao.tmktaicalendar.mmcalendar.MyanmarDate;

public record Holiday (String holiday, LocalDate engDate, MyanmarDate myanmarDate) {}

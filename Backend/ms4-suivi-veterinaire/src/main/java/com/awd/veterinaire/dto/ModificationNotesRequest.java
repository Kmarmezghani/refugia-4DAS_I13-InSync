package com.awd.veterinaire.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ModificationNotesRequest(@JsonProperty(required = true) String notesGenerales) {
}

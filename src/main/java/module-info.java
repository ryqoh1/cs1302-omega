/**
 * Provides the starter code for the <strong>cs1302-omega</strong> project.
 */
module cs1302.omega {
    requires transitive java.logging;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.web;
    exports cs1302.game;
    exports cs1302.omega;
} // module

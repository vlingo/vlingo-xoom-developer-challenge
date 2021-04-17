# 1.  Record architecture decisions

Date: 2021-04-04

## Status

Accepted

## Context

Use gradle as build system rather than maven.

## Decision

The VLINGO XOOM generate a maven project, and we want to challenge the platform using gradle as build system.
The solution will be a gradle project with multi modules representing each bounded context.
And will use JAVA 11 version instead of JAVA 8 version.

## Consequences

All generated maven projects will be converted a gradle project supporting JAVA 11 version.
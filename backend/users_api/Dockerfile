# ─────────────────────────────────────────────────────────────────────────────
#  Users API Dockerfile (ASP.NET Core)
#
#  The source code for this API lives in the original repo:
#    https://github.com/HairdressingProject/styleme/tree/master/Backend/UsersAPI
#
#  Clone the original repo, copy the UsersAPI folder here, then build.
#
#  docker build -t styleme-users-api .
# ─────────────────────────────────────────────────────────────────────────────

FROM mcr.microsoft.com/dotnet/sdk:7.0 AS build
WORKDIR /src

# Copy project file and restore dependencies
COPY *.csproj ./
RUN dotnet restore

# Copy everything and build
COPY . .
RUN dotnet publish -c Release -o /app/publish

# ── Runtime image ─────────────────────────────────────────────────────────────
FROM mcr.microsoft.com/dotnet/aspnet:7.0 AS runtime
WORKDIR /app

COPY --from=build /app/publish .

# Health check
HEALTHCHECK --interval=15s --timeout=5s --retries=5 \
  CMD curl -f http://localhost:5000/health || exit 1

EXPOSE 5000
ENTRYPOINT ["dotnet", "UsersAPI.dll"]

import {Card, CardContent} from "@/components/ui/card.tsx";
import {Button} from "@/components/ui/button.tsx";
import {useTranslation} from "react-i18next";
import {AccountTypeEnum} from "@/types/Users.ts";
import {Badge} from "@/components/ui/badge.tsx";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import {Loader2} from "lucide-react";

const allUserLevels: AccountTypeEnum[] = [AccountTypeEnum.ADMIN, AccountTypeEnum.STAFF, AccountTypeEnum.CLIENT];

// @ts-expect-error idk
function UserLevelsForm({managedUser, handleRemoveClick, handleAddClick, isAlertDialogOpen, setAlertDialogOpen, levelToChange, confirmChangeUserLevel, isLoading}) {
    const {t} = useTranslation();

    return (
        <div>
            <Card className="mx-10 w-auto">
                <CardContent>
                    <div className="flex flex-row justify-around">
                        {allUserLevels.map((level: AccountTypeEnum) => (
                            <div key={level} className="flex flex-col items-center m-5 ">
                                <Badge variant="secondary">{t(level)}</Badge>
                                <div className={"pt-2"}>
                                    {managedUser?.userLevelsDto.some((userLevel: {
                                        roleName: string;
                                    }) => userLevel.roleName === level.toUpperCase()) ? (
                                        <Button className="mt-2" variant={"ghost"}
                                                onClick={() => handleRemoveClick(level)}
                                                disabled={isLoading}>
                                            {isLoading ? (
                                                <>
                                                    <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                                                </>
                                            ) : (
                                                t("accountSettings.users.table.settings.account.userLevels.remove")
                                            )}
                                        </Button>
                                    ) : (
                                        <Button className="mt-2" variant={"default"}
                                                onClick={() => handleAddClick(level)}
                                                disabled={isLoading}>
                                            {isLoading ? (
                                                <>
                                                    <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                                                </>
                                            ) : (
                                                t("accountSettings.users.table.settings.account.userLevels.add")
                                            )}
                                        </Button>
                                    )}
                                </div>

                            </div>
                        ))}
                    </div>
                </CardContent>
            </Card>
            <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                    <AlertDialogDescription>
                        {t("accountSettings.users.table.settings.block.confirm1")}
                        {managedUser?.userLevelsDto.some((userLevel: {
                            roleName: string;
                        }) => userLevel.roleName === levelToChange?.toUpperCase())
                            ? t("accountSettings.users.table.settings.account.userLevels.remove2") :
                            t("accountSettings.users.table.settings.account.userLevels.add2")} {levelToChange}
                        {t("accountSettings.users.table.settings.block.level")}
                        {managedUser?.userLevelsDto.some((userLevel: {
                            roleName: string;
                        }) => userLevel.roleName === levelToChange?.toUpperCase())
                            ? t("accountSettings.users.table.settings.block.from") : t("accountSettings.users.table.settings.block.to")}
                        {t("accountSettings.users.table.settings.block.confirm2")}
                    </AlertDialogDescription>
                    <AlertDialogAction
                        onClick={confirmChangeUserLevel}>{t("general.ok")}</AlertDialogAction>
                    <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                </AlertDialogContent>
            </AlertDialog>
        </div>
    );
}

export default UserLevelsForm;
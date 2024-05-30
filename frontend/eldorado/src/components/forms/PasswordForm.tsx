import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Loader2} from "lucide-react";
import {useTranslation} from "react-i18next";
import {Input} from "@/components/ui/input.tsx";
import {Card, CardContent} from "@/components/ui/card.tsx";
import {
    AlertDialog, AlertDialogAction, AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";

// @ts-expect-error idk
function PasswordForm({formPassword, onSubmitPassword, isLoading, isAlertDialogOpen, setAlertDialogOpen, handleDialogAction}) {
    const {t} = useTranslation();

    return (
        <div>
            <Card className="mx-10 w-auto">
                <CardContent>
                    <Form {...formPassword}>

                            <form onSubmit={formPassword.handleSubmit(onSubmitPassword)}
                                  className="space-y-4">
                                <div className="grid gap-4 p-5">
                                    <div className="grid gap-2">
                                        <FormField
                                            control={formPassword.control}
                                            name="oldPassword"
                                            render={({field}) => (
                                                <FormItem>
                                                    <FormLabel
                                                        className="text-black">{t("accountSettings.authentication.oldPassword")} *</FormLabel>
                                                    <FormControl>
                                                        <Input type="password" {...field} />
                                                    </FormControl>
                                                    <FormMessage/>
                                                </FormItem>
                                            )}
                                        />
                                    </div>
                                    <div className="grid gap-2">
                                        <FormField
                                            control={formPassword.control}
                                            name="newPassword"
                                            render={({field}) => (
                                                <FormItem>
                                                    <FormLabel
                                                        className="text-black">{t("accountSettings.authentication.newPassword")} *</FormLabel>
                                                    <FormControl>
                                                        <Input type="password" {...field} />
                                                    </FormControl>
                                                    <FormMessage/>
                                                </FormItem>
                                            )}
                                        />
                                    </div>
                                    <div className="grid gap-2">
                                        <FormField
                                            control={formPassword.control}
                                            name="newPasswordRepeat"
                                            render={({field}) => (
                                                <FormItem>
                                                    <FormLabel
                                                        className="text-black">{t("accountSettings.authentication.newPasswordRepeat")} *</FormLabel>
                                                    <FormControl>
                                                        <Input type="password" {...field} />
                                                    </FormControl>
                                                    <FormMessage/>
                                                </FormItem>
                                            )}
                                        />
                                    </div>
                                    <Button type="submit" className="w-full pb-2"
                                            disabled={isLoading}>
                                        {isLoading ? (
                                            <>
                                                <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                                            </>
                                        ) : (
                                            t("accountSettings.authentication.password.change")
                                        )}
                                    </Button>
                                </div>
                            </form>

                    </Form>
                </CardContent>
            </Card>
            <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                    <AlertDialogDescription>
                        {t("accountSettings.confirmPasswordChange")}
                    </AlertDialogDescription>
                    <AlertDialogAction onClick={handleDialogAction}>
                        {t("general.ok")}
                    </AlertDialogAction>
                    <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                </AlertDialogContent>
            </AlertDialog>
        </div>
    );
}

export default PasswordForm;